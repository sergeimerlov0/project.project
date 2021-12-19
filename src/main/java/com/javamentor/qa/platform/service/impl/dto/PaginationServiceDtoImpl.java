package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.PageDto;
import org.springframework.stereotype.Component;
import java.util.List;

//Сервис для конструирования страницы PageDTO<T>
@Component
public class PaginationServiceDtoImpl<T> {

    public PageDto<T> getPageDto(int currentPageNumber, int itemsOnPage, List<T> items, int totalResultCount) {
        int totalPageCount = totalResultCount%itemsOnPage == 0 ? totalResultCount/itemsOnPage : totalResultCount/itemsOnPage + 1;
        return new PageDto<> (currentPageNumber, totalPageCount, totalResultCount, items, itemsOnPage);
    }
}
