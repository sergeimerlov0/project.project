package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/question/{questionId}/answer")
@Api(value = "Работа с ответами на вопросы", tags = {"Ответ на вопрос"})
public class ResourceAnswerController {

    private final AnswerService answerService;
    private final AnswerDtoService answerDtoService;

    @ApiOperation(value = "Удаление ответа на вопрос", tags = {"Удаление ответа"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное удаление"),
            @ApiResponse(code = 400, message = "Ответа с таким ID не существует")})
    @DeleteMapping("/{answerId}")
    public ResponseEntity<String> deleteAnswerById(@ApiParam("Id ответа") @PathVariable Long answerId) {
        if (!answerService.existsById(answerId)) {
            return ResponseEntity.badRequest().body("Answer with this ID was not found");
        }
        answerService.deleteById(answerId);
        return ResponseEntity.status(HttpStatus.OK).body("Answer successfully deleted");
    }

    @ApiOperation(value = "Получение списка ответов на вопрос", tags = {"Получение списка ответов"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение")})
    @GetMapping()
    public ResponseEntity<List<AnswerDto>> getAnswerByQuestionId(@PathVariable Long questionId) {
        return new ResponseEntity<>(answerDtoService.getAnswerByQuestionId(questionId), HttpStatus.OK);
    }

}
