package com.javamentor.qa.platform.service.search.impl;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.service.search.manager.abstrats.GlobalSearchManager;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.impl.dto.PaginationServiceDtoImpl;
import com.javamentor.qa.platform.service.search.abstracts.GlobalSearchService;
import com.javamentor.qa.platform.service.search.manager.impl.GlobalSearchByDateFilterConversion;
import com.javamentor.qa.platform.service.search.manager.impl.GlobalSearchByScoreFilterConversion;
import com.javamentor.qa.platform.service.search.manager.impl.GlobalSearchManagerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GlobalSearchServiceImpl extends PaginationServiceDtoImpl<QuestionViewDto> implements GlobalSearchService {


    private final QuestionDtoService questionDtoService;


    @Override
    @Transactional
    public PageDto<QuestionViewDto> getResultPageGlobalSearch(int itemsOnPage, int currentPageNumber, Map<String, Object> map) {
        GlobalSearchManager manager = new GlobalSearchManagerImpl();
        String parseStr = (String) map.get("parseStr");
        List<String> parserResult = (manager.filter(parseStr));
        for (String s : parserResult) {
            if (!s.equals(Optional.empty().toString())) {
                map.put("class", s);
                if (s.equals("QuestionPageDtoByScore")){
                    GlobalSearchByScoreFilterConversion conv = new GlobalSearchByScoreFilterConversion();
                    return questionDtoService.getPageDto(itemsOnPage, currentPageNumber, conv.filterConversion(map));
                } else if (s.equals("QuestionPageDtoByData")){
                    GlobalSearchByDateFilterConversion conv = new GlobalSearchByDateFilterConversion();
                    return questionDtoService.getPageDto(itemsOnPage, currentPageNumber, conv.filterConversion(map));
                }
                return questionDtoService.getPageDto(itemsOnPage, currentPageNumber, map);
            }
        }
        map.put("class", "QuestionPageDtoBasicSearch");
        return questionDtoService.getPageDto(itemsOnPage, currentPageNumber, map);
    }
}

















