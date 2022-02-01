package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.webapp.controllers.dto.UserRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Api("Rest Controller to get a User by ID")
public class UserResourceController {

    private final UserDtoService userDtoService;

    private UserService userService;

    @Autowired
    public UserResourceController(UserDtoService userDtoService, UserService userservice) {
        this.userDtoService = userDtoService;
        this.userService = userservice;
    }

    @GetMapping("/{userId}")
    @ApiOperation("Получение пользователя по ID")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        return userDtoService.getUserById(userId).isEmpty() ?
                new ResponseEntity<>("User with id " + userId + " not found!", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(userDtoService.getUserById(userId), HttpStatus.OK);
    }

    @ApiOperation(value = "Get users by Date Register")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PageDto.class),
            @ApiResponse(code = 400, message = "UserDto not exist")})
    @GetMapping("/new")
    public ResponseEntity<PageDto<UserDto>> getUserByReg(@RequestParam("page") int currentPageNumber,
                                                         @RequestParam(value = "items", defaultValue = "10", required = false) Integer itemsOnPage) {
        //Здесь забираем параметры из запроса currentPageNumber и itemsOnPage
        Map<String, Object> objectMap = new HashMap<>();
        //Помещаем в мапу под ключ class нужный бин с нужной реализацией пагинации. Например, AllUser.
        objectMap.put("class", "RegUser");
        objectMap.put("page", currentPageNumber);
        objectMap.put("items", itemsOnPage);
        return ResponseEntity.ok(userDtoService.getPageDto(currentPageNumber, itemsOnPage, objectMap));
    }

    @GetMapping("/vote")
    @ApiOperation(value = "Получение всех UserDTO с пагинацией отсортированные по голосам")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PageDto.class),
            @ApiResponse(code = 400, message = "UserDTO не найдены")
    })
    public ResponseEntity<PageDto<UserDto>> getAllUserDtoSortDTO(@RequestParam int currentPageNumber,
                                                              @RequestParam(defaultValue = "10") int itemsOnPage){
        Map<String, Object> paginationMap = new HashMap<>();
        paginationMap.put("class", "AllUserDtoSortVote");
        paginationMap.put("currentPageNumber", currentPageNumber);
        paginationMap.put("itemsOnPage", itemsOnPage);

        return ResponseEntity.ok(userDtoService.getPageDto(currentPageNumber, itemsOnPage, paginationMap));
    }
}
