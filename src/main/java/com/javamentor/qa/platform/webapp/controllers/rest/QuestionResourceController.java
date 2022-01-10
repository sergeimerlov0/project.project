package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/question/")
@Api(value = "Работа с вопросами", tags = {"Вопросы"})
public class QuestionResourceController {

    private final QuestionDtoService questionDtoService;
    private final QuestionService questionService;
    private final TagService tagService;
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Список QuestionCommentDto успешно получен"),
            @ApiResponse(code = 404, message = "Вопрос с таким ID не найден")
    })
    public ResponseEntity<?> getQuestionCommentById(@PathVariable Long id) {
        return questionService.getById(id).isPresent() ?
                new ResponseEntity<>(questionDtoService.getQuestionCommentByQuestionId(id), HttpStatus.OK) :
                new ResponseEntity<>("Question with id " + id + " not found!", HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    @ApiOperation(value = "Add a new question", tags = {"Question"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful question creation"),
            @ApiResponse(code = 400, message = "Validation error")
    })
    public ResponseEntity<?> createQuestion(@Validated @RequestBody QuestionCreateDto questionCreateDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        List<Tag> tags = questionCreateDto.getTags().stream()
                .map(tagDto -> tagService
                        .getByName(tagDto.getName())
                        .orElseGet(() -> {
                            Tag tag = new Tag();
                            tag.setName(tagDto.getName());
                            tagService.persist(tag);
                            return tag;
                        }))
                .collect(Collectors.toList());

        Question question = new Question();
        question.setTitle(questionCreateDto.getTitle());
        question.setDescription(questionCreateDto.getDescription());
        question.setUser(user);
        question.setTags(tags);

        // временно устанавливаю ответ пока не исправили баг с getQuestionDtoByQuestionId,
        // который находит только те вопросы, которые имеют хотя бы один ответ
        Answer a = new Answer();
        a.setUser(user);
        a.setQuestion(question);
        a.setHtmlBody("body");
        a.setIsDeleted(false);
        a.setIsHelpful(false);
        a.setIsDeletedByModerator(false);
        question.setAnswers(List.of(a));

        questionService.persist(question);

        return new ResponseEntity<>(questionDtoService.getQuestionDtoByQuestionId(question.getId()), HttpStatus.OK);
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

