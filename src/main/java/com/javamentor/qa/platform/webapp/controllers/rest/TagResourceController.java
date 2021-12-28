package com.javamentor.qa.platform.webapp.controllers.rest;


import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import io.swagger.annotations.Api;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.IgnoredTagService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.abstracts.model.TrackedTagService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @ApiOperation(value = "Добавление тега в TrackedTag", tags = {"TrackedTag"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тег успешно добавлен в TrackedTag"),
            @ApiResponse(code = 400, message = "Тега не существует")})
    @PostMapping("/{id}/tracked")
    public ResponseEntity<List<TagDto>> addTrackedTag(Authentication authentication, @PathVariable Long id) {
        Long userId = 2L; // todo убрать когда будет готово секьюрити
        TrackedTag trackedTag = new TrackedTag();
        User user = userService.getById(userId).get();
        Tag tag = tagService.getById(id).get();
        trackedTag.setTrackedTag(tag);
        trackedTag.setUser(user);
        List<TrackedTag> allTrackedTags = trackedTagService.getAll();
        List<TrackedTag> trackedTagsByUser = new ArrayList<>();
        for (TrackedTag eachTrackedTag : allTrackedTags) {
            if (eachTrackedTag.getUser().getId() == userId) {
                trackedTagsByUser.add(eachTrackedTag);
            }
        }

        for (TrackedTag trackedTagByUser: trackedTagsByUser) {
            if (trackedTagByUser.getTrackedTag().getId() == trackedTag.getTrackedTag().getId()) {
                return new ResponseEntity<>(tagDtoService
                        .getTrackedTagById(userService.getById(userId).get().getId()), HttpStatus.BAD_REQUEST);
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
        Long userId = 2L; // todo убрать когда будет готово секьюрити
        IgnoredTag ignoredTag = new IgnoredTag();
        User user = userService.getById(userId).get();
        Tag tag = tagService.getById(id).get();
        ignoredTag.setIgnoredTag(tag);
        ignoredTag.setUser(user);
        List<IgnoredTag> allIgnoredTags = ignoredTagService.getAll();
        List<IgnoredTag> ignoredTagsByUser = new ArrayList<>();
        for (IgnoredTag eachIgnoredTag : allIgnoredTags) {
            if (eachIgnoredTag.getUser().getId() == userId) {
                ignoredTagsByUser.add(eachIgnoredTag);
            }
        }

        for (IgnoredTag ignoredTagByUser: ignoredTagsByUser) {
            if (ignoredTagByUser.getIgnoredTag().getId() == ignoredTag.getIgnoredTag().getId()) {
                return new ResponseEntity<>(tagDtoService
                        .getIgnoreTagById(userService.getById(userId).get().getId()), HttpStatus.BAD_REQUEST);
            }
        }
        ignoredTagService.persist(ignoredTag);
        return new ResponseEntity<>(tagDtoService
                .getIgnoreTagById(userService.getById(userId).get().getId()), HttpStatus.OK);
    }
}
