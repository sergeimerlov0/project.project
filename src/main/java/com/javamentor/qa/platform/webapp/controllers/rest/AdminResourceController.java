package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
@Api(value = "Security")
public class AdminResourceController {

    private final UserService userService;

    @ApiOperation(value = "Права доступа и авторизации пользователя", tags = {"isEnabled"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение"),
            @ApiResponse(code = 400, message = "Ошибка выполнения")})
    @PostMapping("/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
        Optional<User> optionalUser = userService.getByEmail(email);
        if (optionalUser.isPresent()) {
            userService.deleteByEmail(email);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("User with email " + email + " not found");
    }
}