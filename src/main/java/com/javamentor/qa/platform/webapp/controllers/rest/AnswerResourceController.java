package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/question/{questionId}/answer")
@Api(value = "Работа с ответами на вопросы", tags = {"Ответ на вопрос"})
public class AnswerResourceController {

    private final AnswerService answerService;
    private final AnswerDtoService answerDtoService;
    private final VoteAnswerService voteAnswerService;

    @ApiOperation(value = "Удаление ответа на вопрос", tags = {"Удаление ответа"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное удаление"),
            @ApiResponse(code = 400, message = "Ответа с таким ID не существует")})
    @DeleteMapping("/{answerId}")
    public ResponseEntity<String> deleteAnswerById(@ApiParam("Id ответа") @PathVariable Long answerId) {
        Optional<Answer> optionalAnswer = answerService.getById(answerId);
        if (optionalAnswer.isEmpty()) {
            return ResponseEntity.badRequest().body("Answer with this ID was not found");
        }
        Answer answer = optionalAnswer.get();
        answerService.delete(answer);
        return ResponseEntity.status(HttpStatus.OK).body("Answer successfully deleted");
    }

    @ApiOperation(value = "Получение списка ответов на вопрос", tags = {"Получение списка ответов"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение")})
    @GetMapping()
    public ResponseEntity<List<AnswerDto>> getAnswerByQuestionId(@PathVariable Long questionId) {
        return new ResponseEntity<>(answerDtoService.getAnswerByQuestionId(questionId), HttpStatus.OK);
    }

    @ApiOperation(value = "Голосование за ответ", tags = {"Получение общего количества голосов"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное голосование"),
            @ApiResponse(code = 400, message = "Ошибка голосования")})
    @PostMapping("/{id}/upVote")
    public ResponseEntity<?> setUpVoteAnswerByAnswerId(@PathVariable("id") Long answerId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Optional<Answer> optionalAnswer = answerService.getById(answerId);
        if (optionalAnswer.isEmpty()) {
            return new ResponseEntity<>("Answer with id " + answerId + " not found", HttpStatus.BAD_REQUEST);
        }
        Answer answer = optionalAnswer.get();
        if (Objects.equals(answer.getUser().getId(), user.getId())) {
            return new ResponseEntity<>("Voting for your answer with id " + answerId + " not allowed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(voteAnswerService.postVoteUp(user, answer), HttpStatus.OK);
    }

    @ApiOperation(value = "Голосование против ответа", tags = {"Получение общего количества голосов"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное голосование"),
            @ApiResponse(code = 400, message = "Ошибка голосования")})
    @PostMapping("/{id}/downVote")
    public ResponseEntity<?> setDownVoteAnswerByAnswerId(@PathVariable("id") Long answerId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Optional<Answer> optionalAnswer = answerService.getById(answerId);
        if (optionalAnswer.isEmpty()) {
            return new ResponseEntity<>("Answer with id " + answerId + " not found", HttpStatus.BAD_REQUEST);
        }
        Answer answer = optionalAnswer.get();
        if (Objects.equals(answer.getUser().getId(), user.getId())) {
            return new ResponseEntity<>("Voting for your answer with id " + answerId + " not allowed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(voteAnswerService.postVoteDown(user, answer), HttpStatus.OK);
    }
}
