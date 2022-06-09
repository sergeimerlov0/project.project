package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.service.abstracts.dto.ChatDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.PaginationServiceDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

//Сервис для конструирования страницы PageDTO<T>
@Service
public class PaginationServiceDtoImpl<T> implements PaginationServiceDto<T> {

    private HashMap<String, PaginationDtoAble<T>> map;
    private ChatDtoService chatDtoService;

    @Autowired
    public void setMap(HashMap<String, PaginationDtoAble<T>> map) {
        this.map = map;
    }



    @Override
    @Transactional
    public PageDto<T> getPageDto(int currentPageNumber, int itemsOnPage, Map<String, Object> param) {
        //Здесь мы достаем нужную пагинацию, которая была помещена в контроллере по ключу class, и собираем PageDto
        PaginationDtoAble<T> dtoAble = map.get(param.get("class"));
        int totalResultCount = dtoAble.getTotalResultCount(param);
        int totalPageCount = totalResultCount%itemsOnPage == 0 ? totalResultCount/itemsOnPage : totalResultCount/itemsOnPage + 1;
        return new PageDto<> (currentPageNumber, totalPageCount, totalResultCount, dtoAble.getItems(param), itemsOnPage);
    }


}