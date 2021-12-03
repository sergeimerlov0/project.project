package com.javamentor.qa.platform.webapp.controllers.rest;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/question/{questionId}/answer")
@Api(value = "Работа с ответами на вопросы", tags = {"Ответ на вопрос"})
public class ResourceAnswerController {

    @ApiOperation(value = "Удаление ответа на вопрос", tags = {"Удаление ответа"})
    @ApiResponses(value =
                @ApiResponse(code = 200, message = "Успешное удаление"))
    @DeleteMapping("/{answerId}")
    public void deleteAnswerById(@PathVariable Long answerId, @PathVariable Long questionId) {

    }
}
