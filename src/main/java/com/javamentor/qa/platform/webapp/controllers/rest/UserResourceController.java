package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.BookmarksDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.BookmarksDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserResourceController {
    private final UserDtoService userDtoService;
    private UserService userService;
    private final BookmarksDtoService bookmarksDtoService;

    @Autowired
    public UserResourceController(UserDtoService userDtoService, UserService userservice,
                                  BookmarksDtoService bookmarksDtoService) {
        this.userDtoService = userDtoService;
        this.userService = userservice;
        this.bookmarksDtoService = bookmarksDtoService;
    }


    @GetMapping("/{userId}")
    @ApiOperation("Получение пользователя по ID")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        return userDtoService.getUserById(userId).isEmpty() ?
                new ResponseEntity<>("User with id " + userId + " not found!", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(userDtoService.getUserById(userId), HttpStatus.OK);
    }

    @ApiOperation(value = "Получение пользователей по дате регистрации")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PageDto.class),
            @ApiResponse(code = 400, message = "UserDto not exist")})
    @GetMapping("/new")
    public ResponseEntity<PageDto<UserDto>> getUserByReg(@RequestParam("currentPageNumber") int currentPageNumber,
                                                         @RequestParam(value = "itemsOnPage", defaultValue = "10", required = false) Integer itemsOnPage,
                                                         @RequestParam(value = "filter", defaultValue = "", required = false) String filter) {
        //Здесь забираем параметры из запроса currentPageNumber и itemsOnPage
        Map<String, Object> objectMap = new HashMap<>();
        //Помещаем в мапу под ключ class нужный бин с нужной реализацией пагинации. Например, AllUser.
        objectMap.put("class", "RegUser");
        objectMap.put("currentPageNumber", currentPageNumber);
        objectMap.put("itemsOnPage", itemsOnPage);
        objectMap.put("filter", filter);
        return ResponseEntity.ok(userDtoService.getPageDto(currentPageNumber, itemsOnPage, objectMap));
    }

    @GetMapping("/vote")
    @ApiOperation(value = "Получение всех UserDTO с пагинацией отсортированные по голосам")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PageDto.class),
            @ApiResponse(code = 400, message = "UserDTO не найдены")
    })
    public ResponseEntity<PageDto<UserDto>> getAllUserDtoSortDTO(@RequestParam int currentPageNumber,
                                                                 @RequestParam(defaultValue = "10") int itemsOnPage,
                                                                 @RequestParam(value = "filter", defaultValue = "", required = false) String filter) {
        Map<String, Object> paginationMap = new HashMap<>();
        paginationMap.put("class", "AllUserDtoSortVote");
        paginationMap.put("currentPageNumber", currentPageNumber);
        paginationMap.put("itemsOnPage", itemsOnPage);
        paginationMap.put("filter", filter);
        return ResponseEntity.ok(userDtoService.getPageDto(currentPageNumber, itemsOnPage, paginationMap));
    }

    @GetMapping("/reputation")
    @ApiOperation(value = "Получение всех UserDTO с пагинацией отсортированные по репутации")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PageDto.class),
            @ApiResponse(code = 400, message = "UserDTO не найдены")
    })
    public ResponseEntity<PageDto<UserDto>> getAllUserDtoSortReputation(@RequestParam int currentPageNumber,
                                                                        @RequestParam(defaultValue = "10") int itemsOnPage) {
        Map<String, Object> paginationMap = new HashMap<>();
        paginationMap.put("class", "AllUsersSortedByReputation");
        paginationMap.put("currentPageNumber", currentPageNumber);
        paginationMap.put("itemsOnPage", itemsOnPage);
        return ResponseEntity.ok(userDtoService.getPageDto(currentPageNumber, itemsOnPage, paginationMap));
    }

    @PutMapping(value = "/{userId}/change/password")
    @ApiOperation("Смена пароля пользователя с шифрованием")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пароль изменён"),
            @ApiResponse(code = 400, message = "Пароль не соответствует требованиям безопасности с расшифровкой"),
            @ApiResponse(code = 404, message = "Неверный ID пользователя"),
    })
    public ResponseEntity<?> updatePasswordByEmail(@PathVariable("userId") long userId, @RequestBody String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (!user.getId().equals(userId)) {
            return new ResponseEntity<>("Неверный ID пользователя", HttpStatus.NOT_FOUND);
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            return new ResponseEntity<>("Новый пароль совпадает с текущим", HttpStatus.BAD_REQUEST);
        }

        Map<Pattern, String> conditions = Map.of(
                Pattern.compile(".*[a-z].*"), "строчные буквы",
                Pattern.compile(".*[A-Z].*"), "прописные буквы",
                Pattern.compile(".*\\d.*"), "цифры",
                Pattern.compile(".*[!@#$%].*"), "спецсимволы",
                Pattern.compile(".{6,}"), "не менее 6 символов");

        Optional<String> stringOptional = conditions.entrySet().stream()
                .filter(e -> !password.matches(e.getKey().toString()))
                .map(Map.Entry::getValue).reduce((x, y) -> x + ", " + y);

        if (stringOptional.isPresent()) {
            return new ResponseEntity<>("Пароль должен содержать " + stringOptional.get(), HttpStatus.BAD_REQUEST);
        }

        userService.updatePasswordByEmail(user.getEmail(), passwordEncoder.encode(password));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);
        return new ResponseEntity<>("Пароль пользователя изменён", HttpStatus.OK);

    }

    @GetMapping("/{userId}/profile/delete/questions")
    @ApiOperation("Получение удаленных вопросов пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получен список удаленных вопросов"),
            @ApiResponse(code = 400, message = "Для данного пользователя список удаленных вопросов отсутствует"),
            @ApiResponse(code = 404, message = "Неверный ID пользователя"),
    })
    public ResponseEntity<?> getDeletedQuestionsByUser(@PathVariable("userId") long userId) {
        return userDtoService.getAllDeletedQuestionsByUserId(userId).isEmpty() ?
                new ResponseEntity<>("Для данного пользователя список удаленных вопросов отсутствует", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(userDtoService.getAllDeletedQuestionsByUserId(userId), HttpStatus.OK);

    }

    @GetMapping("/{userId}/profile/questions")
    @ApiOperation("Получение вопросов пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получен список вопросов"),
            @ApiResponse(code = 400, message = "Для данного пользователя список вопросов отсутствует"),
            @ApiResponse(code = 404, message = "Неверный ID пользователя"),
    })
    public ResponseEntity<?> getQuestionsByUser(@PathVariable("userId") long userId){
        return userDtoService.getUserProfileQuestionDtoAddByUserId(userId).isEmpty() ?
                new ResponseEntity<>("Для данного пользователя список вопросов отсутствует", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(userDtoService.getUserProfileQuestionDtoAddByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/profile/bookmarks")
    @ApiOperation(value = "Получение всех закладок BookmarksDto в профиле пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "закладки не найдены"),
            @ApiResponse(code = 404, message = "Неверный ID пользователя")
    })
    public ResponseEntity<List<BookmarksDto>> getBookmarksDtoByUserId(@PathVariable("userId") long userId) {
        return ResponseEntity.ok(bookmarksDtoService.getBookmarksDtoByUserId(userId));
    }

    @GetMapping(value = "/top10ByAnswerPerWeek")
    @ApiOperation(value = "Получение топ 10 пользователей по ответам за неделю", tags = {"Получение пользователей"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение пользователей"),
            @ApiResponse(code = 400, message = "Ошибка получения пользователей")})
    public ResponseEntity<?> getTop10ByAnswerPerWeek(){
        return ResponseEntity.ok(userDtoService.getTop10ByAnswerPerWeek());
    }

}