package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.PaginationDtoAble;
import com.javamentor.qa.platform.service.abstracts.dto.PaginationServiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

//Сервис для конструирования страницы PageDTO<T>
@Component
public class PaginationServiceDtoImpl<T> implements PaginationServiceDto<T> {

    private PaginationDtoAble<T> paginationDtoAble;

    @Autowired
    public void setPaginationDtoAble(PaginationDtoAble<T> paginationDtoAble) {
        this.paginationDtoAble = paginationDtoAble;
    }

    @Override
    public PageDto<T> getPageDto(int currentPageNumber, int itemsOnPage,  Map<String, Object> param) {
        int totalResultCount = paginationDtoAble.getTotalResultCount(param);
        List<T> items = paginationDtoAble.getItems(param);
        int totalPageCount = totalResultCount%itemsOnPage == 0 ? totalResultCount/itemsOnPage : totalResultCount/itemsOnPage + 1;
        return new PageDto<> (currentPageNumber, totalPageCount, totalResultCount, items, itemsOnPage);
    }
}
