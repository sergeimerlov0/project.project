package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
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
import java.util.Optional;
import java.util.regex.Pattern;

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

    @PutMapping(value = "/{userId}/change/password")
    @ApiOperation("Смена пароля с шифрованием")
    public ResponseEntity<?> updatePasswordByEmail(@PathVariable("userId") long userId, @RequestBody String password) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Optional<User> optionalUser = userService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = optionalUser.get();

        if (user.getId().equals(userId)) {
            Map<Pattern, String> conditions = Map.of(
                    Pattern.compile(".*[a-z].*"), "строчные буквы",
                    Pattern.compile(".*[A-Z].*"), "прописные буквы",
                    Pattern.compile(".*\\d.*"), "цыфры",
                    Pattern.compile(".*[!@#$%].*"), "спецсимволы",
                    Pattern.compile(".{6,}"), "не менее 6 символов");

            Optional<String> stringOptional = conditions.entrySet().stream()
                    .filter(e -> !password.matches(e.getKey().toString()))
                    .map(Map.Entry::getValue).reduce((x, y) -> x + ", " + y);

            if (stringOptional.isPresent()) {
                return new ResponseEntity<>("Пароль должен содержать " + stringOptional.get(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Неверный ID пользователя: " + userId, HttpStatus.BAD_REQUEST);
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            return new ResponseEntity<>("Новый пароль совпадает с текущим", HttpStatus.BAD_REQUEST);
        }

        userService.updatePasswordByEmail(user.getEmail(), passwordEncoder.encode(password));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);

        return new ResponseEntity<>("Пароль изменён ", HttpStatus.OK);
    }
}
