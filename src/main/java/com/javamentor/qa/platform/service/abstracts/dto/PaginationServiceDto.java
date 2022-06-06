package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import java.util.Map;

public interface PaginationServiceDto<T> {
    PageDto<T> getPageDto(int currentPageNumber, int itemsOnPage, Map<String, Object> param);

    MessageDto getMessageDto(int itemsOnPage, int currentPageNumber, Map<String, Object> par);
}