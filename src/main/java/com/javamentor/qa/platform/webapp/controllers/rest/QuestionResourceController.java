package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/question/")
@Api(value = "Работа с вопросами", tags = {"Вопросы"})
public class QuestionResourceController {
    private final QuestionDtoService questionDtoService;

    @GetMapping("{id}")
    @ApiOperation(value = "Получение QuestionDto по Question id", tags = {"Получение QuestionDto"})
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "QuestionDto успешно получено"),
            @ApiResponse(code = 400, message = "Вопрос с таким ID не найден")
    })
    public ResponseEntity<?> getQuestionDtoById(@PathVariable Long id) {
        return questionDtoService.getQuestionDtoByQuestionId(id).isEmpty() ?
                new ResponseEntity<>("Question with id " + id + " not found!", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(questionDtoService.getQuestionDtoByQuestionId(id), HttpStatus.OK);
    }

    @GetMapping("tag/{id}")
    @ApiOperation(value = "Получение QuestionDto по TagId", tags = {"Получение QuestionDto по tagId"})
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "QuestionDto успешно получено"),
            @ApiResponse(code = 400, message = "Вопросы с таким TagId не найдены")
    })
    public ResponseEntity<?> getQuestionDtoByTagId(@PathVariable Long id,
                                                                      @RequestParam int page,
                                                                      @RequestParam(defaultValue="10") int items) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("class","AllQuestionDtoByTagId");
        objectMap.put("tagId", id);
        objectMap.put("currentPageNumber", page);
        objectMap.put("itemsOnPage", items);
        PageDto<QuestionDto> pageDto = questionDtoService.getPageDto(page, items, objectMap);
        if (pageDto.getItems().isEmpty()) {
            return new ResponseEntity<>("Questions with tagId " + id + " not found!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}
