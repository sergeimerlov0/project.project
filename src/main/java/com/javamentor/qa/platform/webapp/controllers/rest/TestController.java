package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
@Api(value = "Endpoints for Retrieving of Test String List.", tags = {"List String"})
public class TestController {

    //Внедряем сервис для работы с нужной Dto
    private UserDtoService userDtoService;

    @Autowired
    public void setUserDtoService(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    @ApiOperation(value = "API to GET of Test String List", notes = "Get all string list", tags = {"TestString"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success retrieve", response = List.class)
    })
    @GetMapping
    public List<String> getTest() {
        return new ArrayList<>();
    }

    /*@GetMapping("/pagination")
    public ResponseEntity<PageDto<UserTestDto>> getTest2() {//Здесь забираем параметры из запроса currentPageNumber и itemsOnPage
        Map<String, Object> objectMap = new HashMap<>();
        //Помещаем в мапу под ключ class нужный бин с нужной реализацией пагинации. Например, AllUser.
        objectMap.put("class","AllUser");
        //Получаем страницу с нужной Dto
        return ResponseEntity.ok(userDtoService.getPageDto(1,3, objectMap));
    }*/
}
