package com.javamentor.qa.platform.webapp.controllers.rest;


import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.service.search.abstracts.GlobalSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/search")
@Api(value = "Контроллер для глобального поиска по сайту")
public class GlobalSearchResourceController {

    private final GlobalSearchService service;

    @GetMapping("")
    @ApiOperation(value = "Получение списка вопросов по критериям поиска")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Поиск прошел успешно"),
            @ApiResponse(code = 400, message = "Что-то пошло не так")
    })
    public ResponseEntity<PageDto<QuestionViewDto>> getQuestionDtoByTagId(@RequestParam String q,
                                                   @RequestParam int page,
                                                   @RequestParam(defaultValue = "10") int items) {

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("class", "QuestionPageDtoFromTitle");
            objectMap.put("parseStr", q);
            objectMap.put("currentPageNumber", page);
            objectMap.put("itemsOnPage", items);
            PageDto<QuestionViewDto> pageDto = service.getResultPageGlobalSearch(page, items, objectMap);
            return ResponseEntity.ok().body(pageDto);
        }

    }


