package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.PaginationDtoAble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//Образец сервиса для конструирования PageDto<User>
@Component
public class ExampleService implements PaginationDtoAble<User> {

    private final PaginationServiceDtoImpl<User> userPaginationServiceDto;

    @Autowired
    public ExampleService(PaginationServiceDtoImpl<User> userPaginationServiceDto) {
        this.userPaginationServiceDto = userPaginationServiceDto;
    }

    //Этот метод вызывается в контроллере, передавая currentPageNumber, itemsOnPage и Map с фронта.
    public PageDto<User> createPageDto(int currentPageNumber, int itemsOnPage, Map<String, Object> param){
        return userPaginationServiceDto.getPageDto(currentPageNumber, itemsOnPage, getItems(param), getTotalResultCount(param));
    }

    //Разработчик должен переопределить в этом сервисе методы getItems и getTotalResultCount
    @Override
    public List<User> getItems(Map<String, Object> param) {
        //Какая-то логика работы с БД
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());
        return userList;
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        //Какая-то логика работы с БД
        return 5;
    }

}
