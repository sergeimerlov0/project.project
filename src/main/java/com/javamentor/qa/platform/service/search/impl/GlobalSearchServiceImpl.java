package com.javamentor.qa.platform.service.search.impl;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.service.search.manager.abstrats.GlobalSearchManager;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.impl.dto.PaginationServiceDtoImpl;
import com.javamentor.qa.platform.service.search.abstracts.GlobalSearchService;
import com.javamentor.qa.platform.service.search.manager.impl.GlobalSearchManagerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GlobalSearchServiceImpl extends PaginationServiceDtoImpl<QuestionViewDto> implements GlobalSearchService {


    private final QuestionDtoService questionDtoService;


    @Override
    @Transactional
    public PageDto<QuestionViewDto> getResultPageGlobalSearch(int itemsOnPage, int currentPageNumber, Map<String, Object> map) {
        GlobalSearchManager manager = new GlobalSearchManagerImpl();
        String parseStr = (String) map.get("parseStr");
        List<String> parserResult = manager.filter(parseStr);
        for (String s : parserResult) {
            if (s != null) {
                map.put("class", s);
                return questionDtoService.getPageDto(itemsOnPage, currentPageNumber, map);
            }

        }
        return null; //вместо null должна быть логика поиска, которая отработает, если все элементы в parserResult равны null
    }
}



