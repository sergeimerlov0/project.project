package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserTestDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api("Rest Contoller to get a User by ID")
public class UserResourceController {

    @Autowired
    private UserDtoService userDtoService;
    private UserDtoTestService userDtoTestService;

    @Autowired
    public void setUserDtoTestServiceService(UserDtoTestService userDtoTestService) {
        this.userDtoTestService = userDtoTestService;
    }

    @GetMapping("/api/user/{userId}")
    @ApiOperation("Получение пользователя по ID")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {

        return userDtoService.getUserById(userId).isEmpty() ?
                new ResponseEntity<>("User with id " + userId + " not found!", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(userDtoService.getUserById(userId), HttpStatus.OK);

    }

    @ApiOperation(value = "Get users by Date Register", tags = {"UserTestDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "UserTestDto not exist")})
    @GetMapping("/paginationRega")
    public ResponseEntity<PageDto<UserTestDto>> getUserRega(@RequestParam("page") int currentPageNumber, @RequestParam(value = "items", required = false) Integer itemsOnPage) {//Здесь забираем параметры из запроса currentPageNumber и itemsOnPage
        Map<String, Object> objectMap = new HashMap<>();
        //Помещаем в мапу под ключ class нужный бин с нужной реализацией пагинации. Например, AllUser.
        objectMap.put("class", "RegaUser");
        //Получаем страницу с нужной Dto
        if (itemsOnPage == null) {
            return ResponseEntity.ok(userDtoTestService.getPageDto(currentPageNumber, 10, objectMap));
        }
        return ResponseEntity.ok(userDtoTestService.getPageDto(currentPageNumber, itemsOnPage, objectMap));
    }

}
