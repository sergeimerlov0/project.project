
package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserTestDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class UserDtoServiceImpl extends PaginationServiceDtoImpl<UserDto>  implements UserDtoService {

    @Autowired
    private UserDtoDao userDtoDao;
//    private Map<String, PaginationDtoAble<UserDto>> map;
//
//    @Autowired
//    public void setMap(Map<String, PaginationDtoAble<UserDto>> map) {
//        this.map = map;
//    }

    @Transactional
    public Optional<UserDto> getUserById(Long id) {
        return userDtoDao.getUserById(id);
    }

//
//    @Override
//    public PageDto<UserDto> getPageDto(int currentPageNumber, int itemsOnPage, Map<String, Object> param) {
//        //Здесь мы достаем нужную пагинацию, которая была помещена в контроллере по ключу class, и собираем PageDto
//        PaginationDtoAble<UserDto> dtoAble = map.get(param.get("class"));
//        int totalResultCount = dtoAble.getTotalResultCount(param);
//        int totalPageCount = totalResultCount%itemsOnPage == 0 ? totalResultCount/itemsOnPage : totalResultCount/itemsOnPage + 1;
//        return new PageDto<> (currentPageNumber, totalPageCount, totalResultCount, dtoAble.getItems(param), itemsOnPage);
//    }
}
