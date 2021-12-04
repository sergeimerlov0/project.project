package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/question/{questionId}/answer")
@Api(value = "Работа с ответами на вопросы", tags = {"Ответ на вопрос"})
public class ResourceAnswerController {
    private final AnswerDao answerDao;

    @ApiOperation(value = "Удаление ответа на вопрос", tags = {"Удаление ответа"})
    @ApiResponses(value =
                @ApiResponse(code = 200, message = "Успешное удаление"))
    @DeleteMapping("/{answerId}")
    public void deleteAnswerById(@ApiParam("Id ответа") @PathVariable Long answerId,
                                 @ApiParam("Id вопроса") @PathVariable Long questionId) {
        answerDao.deleteById(answerId);
    }
}
