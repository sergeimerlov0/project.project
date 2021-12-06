package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.PaginationDtoAble;

import java.util.List;
import java.util.Map;

//Сервис для конструрирования страницы PageDTO<T>
public class DtoPaginationService<T> {

    //Здесь параметры, нужные для работы сервиса
    private PaginationDtoAble<T> paginationDtoAble;
    private int totalResultCount;
    private List<T> items;

    //Через конструктор получаем необходимый для работы объект с интерфейсом PaginationDtoAble и Map с параметрами для
    //вызова методов getItems и getTotalResultCount
    public DtoPaginationService(PaginationDtoAble<T> paginationDtoAble, Map<String, Object> param){
        this.paginationDtoAble = paginationDtoAble;
        totalResultCount = paginationDtoAble.getTotalResultCount(param);
        items =  paginationDtoAble.getItems(param);
    }

    //Через метод конструируем PageDto, получая извне доп параметры такие как currentPageNumber и itemsOnPage
    public PageDto<T> getPageDto(int currentPageNumber, int itemsOnPage) {
        int totalPageCount = totalResultCount%itemsOnPage == 0 ? totalResultCount/itemsOnPage : totalResultCount/itemsOnPage + 1;
        return new PageDto<> (currentPageNumber, totalPageCount, totalResultCount, items, itemsOnPage);
    }


}
