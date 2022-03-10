package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.TagViewDto;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


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
        return ResponseEntity.ok(tagDtoService.getRelatedTagsDto());
    }

    @ApiOperation(value = "Getting all TrackedTagDto", tags = {"TrackedTagDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "TrackedTagDto not exist")})
    @GetMapping("/tracked")
    public ResponseEntity<List<TagDto>> getAllTrackedTagDto(Authentication authentication) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = user.getId();
        return ResponseEntity.ok(tagDtoService.getTrackedTagById(userId));
    }

    @ApiOperation(value = "Getting all IgnoredTagDto", tags = {"IgnoredTagDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "IgnoredTagDto not exist")})
    @GetMapping("/ignored")
    public ResponseEntity<List<TagDto>> getAllIgnoredTagDto(Authentication authentication) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = user.getId();
        return ResponseEntity.ok(tagDtoService.getIgnoreTagById(userId));
    }

    @ApiOperation(value = "Добавление тега в TrackedTag", tags = {"TrackedTag"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тег успешно добавлен в TrackedTag"),
            @ApiResponse(code = 400, message = "Некорректный запрос")})
    @PostMapping("/{id}/tracked")
    @Transactional
    public ResponseEntity<?> addTrackedTag(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = user.getId();
        Optional<Tag> tag = tagService.getById(id);

        if (tag.isPresent()) {
            if (trackedTagService.tagIsPresentInTheListOfUser(userId, id)) {
                return ResponseEntity.badRequest().body("Tag with id found in tracked");
            }
            if (ignoredTagService.tagIsPresentInTheListOfUser(userId, id)) {
                return ResponseEntity.badRequest().body("Tag with id found in ignored");
            }

            TrackedTag trackedTag = new TrackedTag();
            trackedTag.setTrackedTag(tag.get());
            trackedTag.setUser(user);
            trackedTagService.persist(trackedTag);
            return ResponseEntity.ok(tagDtoService.getTrackedTagById(userId));
        }

        return ResponseEntity.badRequest().body("Tag with this ID was not found");
    }

    @ApiOperation(value = "Добавление тега в IgnoredTag", tags = {"IgnoredTag"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тег успешно добавлен в IgnoredTag"),
            @ApiResponse(code = 400, message = "Некорректный запрос")})
    @PostMapping("/{id}/ignored")
    @Transactional
    public ResponseEntity<?> addIgnoredTag(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = user.getId();
        Optional<Tag> tag = tagService.getById(id);

        if (tag.isPresent()) {
            if (trackedTagService.tagIsPresentInTheListOfUser(userId, id)) {
                return ResponseEntity.badRequest().body("Tag with id found in tracked");
            }
            if (ignoredTagService.tagIsPresentInTheListOfUser(userId, id)) {
                return ResponseEntity.badRequest().body("Tag with id found in ignored");
            }

            IgnoredTag ignoredTag = new IgnoredTag();
            ignoredTag.setIgnoredTag(tag.get());
            ignoredTag.setUser(user);
            ignoredTagService.persist(ignoredTag);
            return ResponseEntity.ok(tagDtoService.getIgnoreTagById(userId));

        }

        return ResponseEntity.badRequest().body("Tag with this ID was not found");
    }

    @ApiOperation(value = "Get top 10 tags with a string", tags = {"TagsTop10WithString"})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Not found")
    })
    @GetMapping("/latter")
    public ResponseEntity<List<TagDto>> getTagsTop10WithString(@RequestParam("string") String partTag) {
        return new ResponseEntity<>(tagDtoService.getTagsTop10WithString(partTag), HttpStatus.OK);
    }

    @ApiOperation(value = "Get tags sorted by name with pagination", tags = {"GetAllTagsViewDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PageDto.class),
            @ApiResponse(code = 400, message = "TagViewDto not exist")})
    @GetMapping("/name")
    public ResponseEntity<PageDto<TagViewDto>> getTagsSorted(@RequestParam("page") int currentPageNumber,
                                                             @RequestParam(value = "items", defaultValue = "10", required = false) Integer itemsOnPage) {
        Map<String, Object> paginationMap = new HashMap<>();
        paginationMap.put("class", "TagsViewsSortedByName");
        paginationMap.put("currentPageNumber", currentPageNumber);
        paginationMap.put("itemsOnPage", itemsOnPage);
        return ResponseEntity.ok(tagDtoService.getPageDto(currentPageNumber, itemsOnPage, paginationMap));
    }
}
