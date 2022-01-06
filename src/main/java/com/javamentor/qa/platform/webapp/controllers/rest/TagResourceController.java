package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.IgnoredTagService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.abstracts.model.TrackedTagService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/tag")
@Api(value = "Работа с тэгами на вопросы", tags = {"Тэг и вопросы"})
public class TagResourceController {

    private final TrackedTagService trackedTagService;
    private final IgnoredTagService ignoredTagService;
    private final TagDtoService tagDtoService;
    private final UserService userService;
    private final TagService tagService;

    @ApiOperation(value = "Получение списка из 10 тэгов с " +
            "наибольшим количеством вопросов с данным тэгом", tags = {"Получение списка тэгов"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение")})
    @GetMapping("/related")
    public ResponseEntity<List<RelatedTagsDto>> getRelatedTagDto() {
        return new ResponseEntity<>(tagDtoService.getRelatedTagsDto(), HttpStatus.OK);
    }

    @ApiOperation(value = "Getting all TrackedTagDto", tags = {"TrackedTagDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "TrackedTagDto not exist")})
    @GetMapping("/tracked")
    public ResponseEntity<List<TagDto>> getAllTrackedTagDto(Authentication authentication) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = user.getId();
        return new ResponseEntity<>(tagDtoService
                .getTrackedTagById(userService.getById(userId).get().getId()), HttpStatus.OK);
    }

    @ApiOperation(value = "Getting all IgnoredTagDto", tags = {"IgnoredTagDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "IgnoredTagDto not exist")})
    @GetMapping("/ignored")
    public ResponseEntity<List<TagDto>> getAllIgnoredTagDto(Authentication authentication) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = user.getId();
        return new ResponseEntity<>(tagDtoService
                .getIgnoreTagById(userService.getById(userId).get().getId()), HttpStatus.OK);
    }

    @ApiOperation(value = "Добавление тега в TrackedTag", tags = {"TrackedTag"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тег успешно добавлен в TrackedTag"),
            @ApiResponse(code = 400, message = "Тег уже добавлен")})
    @PostMapping("/{id}/tracked")
    public ResponseEntity<List<TagDto>> addTrackedTag(Authentication authentication, @PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = user.getId();
        TrackedTag trackedTag = new TrackedTag();
        Tag tag = tagService.getById(id).get();
        trackedTag.setTrackedTag(tag);
        trackedTag.setUser(user);

        //Создание списка отслеживаемых тегов принадлежащих авторизованному юзеру
        List<TrackedTag> allTrackedTags = trackedTagService.getAll();
        List<TrackedTag> trackedTagsByUser = new ArrayList<>();
        for (TrackedTag eachTrackedTag : allTrackedTags) {
            if (eachTrackedTag.getUser().getId() == userId) {
                trackedTagsByUser.add(eachTrackedTag);
            }
        }

        //Выбрасывание 400 если в списке отслеживаемых тегов юзера уже имеется добавляемый тег
        for (TrackedTag trackedTagByUser: trackedTagsByUser) {
            if (trackedTagByUser.getTrackedTag().getId() == trackedTag.getTrackedTag().getId()) {
                return new ResponseEntity<>(tagDtoService
                        .getTrackedTagById(userService.getById(userId).get().getId()), HttpStatus.BAD_REQUEST);
            }
        }

        //Создание списка игнорируемых тегов принадлежащих авторизованному юзеру
        List<IgnoredTag> allIgnoredTags = ignoredTagService.getAll();
        List<IgnoredTag> ignoredTagsByUser = new ArrayList<>();
        for (IgnoredTag eachIgnoredTag : allIgnoredTags) {
            if (eachIgnoredTag.getUser().getId() == userId) {
                ignoredTagsByUser.add(eachIgnoredTag);
            }
        }

        //Удаление тега добавленного в отслеживаемые из списка игнорируемых
        for (IgnoredTag ignoredTagByUser: ignoredTagsByUser) {
            if (ignoredTagByUser.getIgnoredTag().getId() == id) {
                ignoredTagService.deleteById(ignoredTagByUser.getId());
            }
        }

        trackedTagService.persist(trackedTag);
        return new ResponseEntity<>(tagDtoService
                .getTrackedTagById(userService.getById(userId).get().getId()), HttpStatus.OK);
    }

    @ApiOperation(value = "Добавление тега в IgnoredTag", tags = {"IgnoredTag"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тег успешно добавлен в IgnoredTag"),
            @ApiResponse(code = 400, message = "Тег уже добавлен")})
    @PostMapping("/{id}/ignored")
    public ResponseEntity<List<TagDto>> addIgnoredTag(Authentication authentication, @PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = user.getId();
        IgnoredTag ignoredTag = new IgnoredTag();
        Tag tag = tagService.getById(id).get();
        ignoredTag.setIgnoredTag(tag);
        ignoredTag.setUser(user);

        //Создание списка игнорируемых тегов принадлежащих авторизованному юзеру
        List<IgnoredTag> allIgnoredTags = ignoredTagService.getAll();
        List<IgnoredTag> ignoredTagsByUser = new ArrayList<>();
        for (IgnoredTag eachIgnoredTag : allIgnoredTags) {
            if (eachIgnoredTag.getUser().getId() == userId) {
                ignoredTagsByUser.add(eachIgnoredTag);
            }
        }

        //Выбрасывание 400 если в списке игнорируемых тегов юзера уже имеется добавляемый тег
        for (IgnoredTag ignoredTagByUser: ignoredTagsByUser) {
            if (ignoredTagByUser.getIgnoredTag().getId() == ignoredTag.getIgnoredTag().getId()) {
                return new ResponseEntity<>(tagDtoService
                        .getIgnoreTagById(userService.getById(userId).get().getId()), HttpStatus.BAD_REQUEST);
            }
        }

        //Создание списка отслеживаемых тегов принадлежащих авторизованному юзеру
        List<TrackedTag> allTrackedTags = trackedTagService.getAll();
        List<TrackedTag> trackedTagsByUser = new ArrayList<>();
        for (TrackedTag eachTrackedTag : allTrackedTags) {
            if (eachTrackedTag.getUser().getId() == userId) {
                trackedTagsByUser.add(eachTrackedTag);
            }
        }

        //Удаление тега добавленного в игнорируемые из списка отслеживаемых
        for (TrackedTag trackedTagByUser: trackedTagsByUser) {
            if (trackedTagByUser.getTrackedTag().getId() == id) {
                trackedTagService.deleteById(trackedTagByUser.getId());
            }
        }

        ignoredTagService.persist(ignoredTag);
        return new ResponseEntity<>(tagDtoService
                .getIgnoreTagById(userService.getById(userId).get().getId()), HttpStatus.OK);
    }
}
