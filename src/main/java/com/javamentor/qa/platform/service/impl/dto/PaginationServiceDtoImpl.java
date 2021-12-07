package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.PaginationServiceDto;
import org.springframework.stereotype.Component;
import java.util.List;

//Сервис для конструирования страницы PageDTO<T>
@Component
public class PaginationServiceDtoImpl<T> implements PaginationServiceDto<T> {

    @Override
    public PageDto<T> getPageDto(int currentPageNumber, int itemsOnPage, int totalResultCount, List<T> items) {
        int totalPageCount = totalResultCount%itemsOnPage == 0 ? totalResultCount/itemsOnPage : totalResultCount/itemsOnPage + 1;
        return new PageDto<> (currentPageNumber, totalPageCount, totalResultCount, items, itemsOnPage);
    }
}
