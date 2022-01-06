package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/question/{questionId}/answer")
@Api(value = "Работа с ответами на вопросы", tags = {"Ответ на вопрос"})
public class AnswerResourceController {

    private final AnswerService answerService;
    private final AnswerDtoService answerDtoService;
    private final VoteAnswerService voteAnswerService;
    private final UserService userService;

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

    @ApiOperation(value = "Голосование за ответ", tags = {"Получение общего количества голосов"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное голосование"),
            @ApiResponse(code = 400, message = "Ошибка голосования")})
    @PostMapping("/{id}/upVote")
    public ResponseEntity<Long> setUpVoteAnswerByAnswerId(@PathVariable("id") Long answerId, Principal principal) {
        Optional<User> optionalUser = userService.getByEmail(principal.getName());
        Optional<Answer> optionalAnswer = answerService.getById(answerId);
        if (optionalUser.isEmpty() || optionalAnswer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User userSender = optionalUser.get();
        Answer answer = optionalAnswer.get();
        return new ResponseEntity<>(voteAnswerService.postVoteUp(answerId, userSender, answer), HttpStatus.OK);
    }

    @ApiOperation(value = "Голосование против ответа", tags = {"Получение общего количества голосов"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное голосование"),
            @ApiResponse(code = 400, message = "Ошибка голосования")})
    @PostMapping("/{id}/downVote")
    public ResponseEntity<Long> setDownVoteAnswerByAnswerId(@PathVariable("id") Long answerId, Principal principal) {
        Optional<User> optionalUser = userService.getByEmail(principal.getName());
        Optional<Answer> optionalAnswer = answerService.getById(answerId);
        if (optionalUser.isEmpty() || optionalAnswer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User userSender = optionalUser.get();
        Answer answer = optionalAnswer.get();
        return new ResponseEntity<>(voteAnswerService.postVoteDown(answerId, userSender, answer), HttpStatus.OK);
    }

}
