package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.PaginationServiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

//Сервис для конструирования страницы PageDTO<T>
@Service
public class PaginationServiceDtoImpl<T> implements PaginationServiceDto<T> {

    private Map<String, PaginationDtoAble<T>> map;

    @Autowired
    public void setMap(Map<String, PaginationDtoAble<T>> map) {
        this.map = map;
    }

    @Override
    public PageDto<T> getPageDto(int currentPageNumber, int itemsOnPage, Map<String, Object> param) {
        //Здесь мы достаем нужную пагинацию, которая была помещена в контроллере по ключу class, и собираем PageDto
        PaginationDtoAble<T> dtoAble = map.get(param.get("class"));
        int totalResultCount = dtoAble.getTotalResultCount(param);
        int totalPageCount = totalResultCount%itemsOnPage == 0 ? totalResultCount/itemsOnPage : totalResultCount/itemsOnPage + 1;
        return new PageDto<> (currentPageNumber, totalPageCount, totalResultCount, dtoAble.getItems(param), itemsOnPage);
    }

}
