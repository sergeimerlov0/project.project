package com.javamentor.qa.platform.service.search.abstracts;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;

import java.util.Map;

public interface GlobalSearchService {
    PageDto<QuestionViewDto> getResultPageGlobalSearch( int itemsOnPage, int currentPageNumber, Map<String, Object> map);
}
