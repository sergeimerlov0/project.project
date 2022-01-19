package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
@Api(value = "Security")
public class AdminResourceController {

    private final UserService userService;

    @ApiOperation(value = "Права доступа и авторизации пользователя", tags = {"isEnabled"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение"),
            @ApiResponse(code = 400, message = "Ошибка выполнения")})
    @PostMapping("/{userId}/isEnabled")
    public ResponseEntity<?> setIsEnabled(@PathVariable("userId") Long userId) {
        Optional<User> optionalUser = userService.getById(userId);
        if (optionalUser.isPresent()) {
            User userById = optionalUser.get();
            if (userById.isEnabled()) {
                userById.setIsEnabled(false);
            } else if (!userById.isEnabled()) {
                userById.setIsEnabled(true);
            }
            userService.update(userById);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}