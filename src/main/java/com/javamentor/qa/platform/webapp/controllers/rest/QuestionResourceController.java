package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionCommentDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/question/")
@Api(value = "Работа с вопросами", tags = {"Вопросы"})
public class QuestionResourceController {

    private final QuestionDtoService questionDtoService;
    private final QuestionService questionService;
    private final QuestionCommentDtoService questionCommentDtoService;
    private final VoteQuestionService voteQuestionService;

    @GetMapping("{id}")
    @ApiOperation(value = "Получение QuestionDto по Question id", tags = {"Получение QuestionDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "QuestionDto успешно получено"),
            @ApiResponse(code = 400, message = "Вопрос с таким ID не найден")
    })
    public ResponseEntity<?> getQuestionDtoById(@PathVariable Long id) {
        return questionDtoService.getQuestionDtoByQuestionId(id).isEmpty() ?
                new ResponseEntity<>("Question with id " + id + " not found!", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(questionDtoService.getQuestionDtoByQuestionId(id), HttpStatus.OK);
    }

    @GetMapping("{id}/comment")
    @ApiOperation(value = "Получение списка QuestionCommentDto по Question id",
            tags = {"список", "комментарий", "вопрос"})
    @ApiResponse(code = 200, message = "Список QuestionCommentDto успешно получен")
    public ResponseEntity<List<QuestionCommentDto>> getQuestionCommentById(@PathVariable Long id) {
        return new ResponseEntity<>(questionCommentDtoService.getQuestionCommentByQuestionId(id), HttpStatus.OK);
    }

    @PostMapping("{questionId}/upVote")
    @ApiOperation(value = "Голосование за Question по Question id", tags = {"VoteQuestion up"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Голосование успешно произведено"),
            @ApiResponse(code = 400, message = "Вопрос с таким ID не найден или Вы уже голосовали за данный Question")
    })
    public ResponseEntity<Integer> upVote(@AuthenticationPrincipal(expression = "@userService.getUser(#this)") User user, @PathVariable Long questionId) {
        Optional<Question> optionalQuestion = questionService.getById(questionId);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (voteQuestionService.userVoteCheck(questionId, user.getId())) {
                VoteQuestion voteQuestion = new VoteQuestion(user, question, LocalDateTime.now(), VoteType.UP_VOTE);
                voteQuestionService.persist(voteQuestion);
                return new ResponseEntity<>(voteQuestionService.getTotalVoteQuestionsByQuestionId(questionId), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("{questionId}/downVote")
    @ApiOperation(value = "Голосование за Question по Question id", tags = {"VoteQuestion down"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Голосование успешно произведено"),
            @ApiResponse(code = 400, message = "Вопрос с таким ID не найден или Вы уже голосовали за данный Question")
    })
    public ResponseEntity<Integer> downVote(@AuthenticationPrincipal User user, @PathVariable Long questionId) {
        Optional<Question> optionalQuestion = questionService.getById(questionId);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (voteQuestionService.userVoteCheck(questionId, user.getId())) {
                VoteQuestion voteQuestion = new VoteQuestion(user, question, LocalDateTime.now(), VoteType.DOWN_VOTE);
                voteQuestionService.persist(voteQuestion);
                return new ResponseEntity<>(voteQuestionService.getTotalVoteQuestionsByQuestionId(questionId), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}

