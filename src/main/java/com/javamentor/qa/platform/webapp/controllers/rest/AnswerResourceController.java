package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerBodyDto;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    private final QuestionService questionService;
    private final ReputationService reputationService;
    private final UserService userService;

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
        return ResponseEntity.ok().body("Answer successfully deleted");
    }

    @ApiOperation(value = "Получение списка ответов на вопрос", tags = {"Получение списка ответов"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение")})
    @GetMapping()
    public ResponseEntity<List<AnswerDto>> getAnswerByQuestionId(@PathVariable Long questionId) {
        return ResponseEntity.ok().body(answerDtoService.getAnswerByQuestionId(questionId));
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
            return ResponseEntity.badRequest().body("Answer with id " + answerId + " not found");
        }
        Answer answer = optionalAnswer.get();
        if (Objects.equals(answer.getUser().getId(), user.getId())) {
            return ResponseEntity.badRequest().body("Voting for your answer with id " + answerId + " not allowed");
        }
        return ResponseEntity.ok().body(voteAnswerService.postVoteUp(user, answer));
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
            return ResponseEntity.badRequest().body("Answer with id " + answerId + " not found");
        }
        Answer answer = optionalAnswer.get();
        if (Objects.equals(answer.getUser().getId(), user.getId())) {
            return ResponseEntity.badRequest().body("Voting for your answer with id " + answerId + " not allowed");
        }
        return ResponseEntity.ok().body(voteAnswerService.postVoteDown(user, answer));
    }

    @ApiOperation(value = "Добавление ответа на вопрос", tags = {"Добавление ответа"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное добавление ответа"),
            @ApiResponse(code = 400, message = "Ошибка добавления ответа")})
    @PostMapping("/add")
    //позволяет добавлять только один ответ
    public ResponseEntity<?> addNewAnswer(@PathVariable Long questionId,
                                          @Valid @RequestBody AnswerBodyDto answerBodyDto) {
        Optional<Question> optionalQuestion = questionService.getById(questionId);
        if (optionalQuestion.isPresent()) {
            if (answerBodyDto.getHtmlBody() != null) {
                Question question = optionalQuestion.get();
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
                if (!answerService.checkAnswerByQuestionIdAndUserId(questionId, user.getId())) {
                    Answer answer = new Answer(question, user, answerBodyDto.getHtmlBody());
                    answerService.persist(answer);
                    return ResponseEntity.ok().body(answerDtoService.getAnswerDtoByAnswerId(answer.getId()));
                }
                return ResponseEntity.badRequest().body("Ответ уже был добавлен");
            }
            return ResponseEntity.badRequest().body("Тело ответа не может быть пустым");
        }
        return ResponseEntity.badRequest().body("Вопроса с  id " + questionId + " не существует");
    }
}
