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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/tag")
@Api(value = "Работа с тэгами на вопросы", tags = {"Тэг и вопросы"})
public class TagResourceController {

    @PersistenceContext
    EntityManager entityManager;

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
    @Transactional
    public ResponseEntity<List<TagDto>> addTrackedTag(Authentication authentication, @PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = user.getId();
        TrackedTag trackedTag = new TrackedTag();
        Tag tag = tagService.getById(id).get();
        trackedTag.setTrackedTag(tag);
        trackedTag.setUser(user);

        //Создание списка отслеживаемых тегов принадлежащих авторизованному юзеру
        List<Long> trackedTagsByUserIds = entityManager
                .createQuery("select t.trackedTag.id from TrackedTag t where t.user.id=:id", Long.class)
                .setParameter("id", userId).getResultList();

        //Выбрасывание 400 если в списке отслеживаемых тегов юзера уже имеется добавляемый тег
        if(trackedTagsByUserIds.contains(trackedTag.getTrackedTag().getId())) {
            return new ResponseEntity<>(tagDtoService
                    .getTrackedTagById(userService.getById(userId).get().getId()), HttpStatus.BAD_REQUEST);
        }

        //Создание списка игнорируемых тегов принадлежащих авторизованному юзеру
        List<Long> allIgnoredTagsIds = entityManager
                .createQuery("select t.ignoredTag.id from IgnoredTag t where t.user.id=:id", Long.class)
                .setParameter("id", userId).getResultList();

        //Удаление тега добавленного в отслеживаемые из списка игнорируемых
        if(allIgnoredTagsIds.contains(id)) {
            entityManager.joinTransaction();
            entityManager.createQuery("delete from IgnoredTag t where t.ignoredTag.id=:id")
                    .setParameter("id", id).executeUpdate();
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
    @Transactional
    public ResponseEntity<List<TagDto>> addIgnoredTag(Authentication authentication, @PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = user.getId();
        IgnoredTag ignoredTag = new IgnoredTag();
        Tag tag = tagService.getById(id).get();
        ignoredTag.setIgnoredTag(tag);
        ignoredTag.setUser(user);

        //Создание списка игнорируемых тегов принадлежащих авторизованному юзеру
        List<Long> ignoredTagsByUserIds = entityManager
                .createQuery("select t.ignoredTag.id from IgnoredTag t where t.user.id=:id", Long.class)
                .setParameter("id", userId).getResultList();

        //Выбрасывание 400 если в списке игнорируемых тегов юзера уже имеется добавляемый тег
        if(ignoredTagsByUserIds.contains(ignoredTag.getIgnoredTag().getId())) {
            return new ResponseEntity<>(tagDtoService
                    .getTrackedTagById(userService.getById(userId).get().getId()), HttpStatus.BAD_REQUEST);
        }

        //Создание списка отслеживаемых тегов принадлежащих авторизованному юзеру
        List<Long> allTrackedTagsIds = entityManager
                .createQuery("select t.trackedTag.id from TrackedTag t where t.user.id=:id", Long.class)
                .setParameter("id", userId).getResultList();

        //Удаление тега добавленного в игнорируемые из списка отслеживаемых
        if(allTrackedTagsIds.contains(id)) {
            entityManager.joinTransaction();
            entityManager.createQuery("delete from TrackedTag t where t.trackedTag.id=:id")
                    .setParameter("id", id).executeUpdate();
        }
        ignoredTagService.persist(ignoredTag);
        return new ResponseEntity<>(tagDtoService
                .getIgnoreTagById(userService.getById(userId).get().getId()), HttpStatus.OK);
    }
}
