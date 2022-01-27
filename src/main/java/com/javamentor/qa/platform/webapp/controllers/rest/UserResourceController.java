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

    @PostMapping("/api/user/{userId}/changepassword")
    @ApiOperation("Смена пароля")
    public ResponseEntity<String> changePassword(@PathVariable int id, @RequestBody UserRequest userRequest) {
        User user = userService.findByIdAndOldPassword(Long.valueOf(id), userRequest.getOldPassword());

        if (user != null) {

            if (!(user.getPassword().equals(userRequest.getOldPassword()))) {
                System.out.println("adaldkalk");
                throw new ConstraintViolationException("System Password and provided Old Password doesn't match", null, null);
            }

            if (StringUtils.isBlank(userRequest.getOldPassword())) {
                throw new ConstraintViolationException("It's mandatory to give old Password, to set a new Password", null, null);
            }

            if (StringUtils.isBlank(userRequest.getNewPassword())) {
                throw new ConstraintViolationException("New Password can not be empty or null", null, null);
            }

            double percentageMatched = StringUtils.getJaroWinklerDistance(user.getPassword(), userRequest.getNewPassword()) * 100;
            if (percentageMatched >= 80) {
                throw new ConstraintViolationException("Percentage of old and new password is equal and more than 80%", null, null);
            }
            //Number of digit present in string
            int validCriteriaForNoOfDigit = userRequest.getNewPassword().length() / 2;
            int digitCounter = 0;
            char[] charArray = userRequest.getNewPassword().toCharArray();
            for (char c : charArray) {
                if (Character.isDigit(c)) {
                    digitCounter++;
                }
            }

            if (digitCounter >= validCriteriaForNoOfDigit) {
                throw new ConstraintViolationException("50% of password is number", null, null);
            }

            // special characters match
            String newPassword = userRequest.getNewPassword();

            String specialChars = "!@#$&*";
            int charCounter = 0;
            for (int i =0; i< newPassword.length(); i++) {
                if (specialChars.contains(newPassword.charAt(i)+"")) {
                    charCounter++;
                }
            }

            if (charCounter > 4) {
                throw new ConstraintViolationException("More than 4 special Characters", null, null);
            }

            user.setPassword(userRequest.getNewPassword()); //setOldPassword
            user.setNewPassword(userRequest.getNewPassword()); // setNewPassword
            userService.save(user);
            return new ResponseEntity<String>("Password updated successfully", HttpStatus.OK);
        }

        return new ResponseEntity<String>("Password not updated", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
