package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/question")
@Api(value = "Работа с вопросами", tags = {"Вопросы"})
public class QuestionResourceController {

    private final QuestionDtoService questionDtoService;
    private final QuestionService questionService;
    private final VoteQuestionService voteQuestionService;
    private final TagService tagService;

    @GetMapping("/{id}")
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

    @GetMapping("/{id}/comment")
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

    @GetMapping("/noAnswer")
    @ApiOperation(value = "Получение QuestionDto, на которые нет ответов", tags = {"Получение QuestionDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "QuestionDto успешно получено"),
            @ApiResponse(code = 400, message = "Параметры заданы неверно")
    })
    public ResponseEntity<?> getQuestionDtoNoAnswer(@RequestParam int page,
                                                    @RequestParam(defaultValue = "10") int items,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                            List<Long> ignoredTags,
                                                    @RequestParam(required = false) List<Long> trackedTags) {
        Map<String, Object> map = new HashMap<>();
        map.put("class", "QuestionDtoNoAnswer");
        map.put("currentPageNumber", page);
        map.put("itemsOnPage", items);
        map.put("ignoredTags", ignoredTags);
        map.put("trackedTags", trackedTags);
        PageDto<QuestionDto> pageDto = questionDtoService.getPageDto(page, items, map);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @PostMapping("/{questionId}/upVote")
    @ApiOperation(value = "Голосование за Question по Question id", tags = {"VoteQuestion up"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Голосование успешно произведено"),
            @ApiResponse(code = 400, message = "Вопрос с таким ID не найден или Вы уже голосовали за данный Question")
    })
    public ResponseEntity<Integer> upVote(@PathVariable Long questionId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Optional<Question> optionalQuestion = questionService.getById(questionId);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (voteQuestionService.userVoteCheck(questionId, user.getId())) {
                VoteQuestion voteQuestion = new VoteQuestion(user, question, LocalDateTime.now(), VoteType.UP_VOTE);
                voteQuestionService.persist(voteQuestion);
                return new ResponseEntity<>(voteQuestionService.getTotalVoteQuestionsByQuestionId(questionId),
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{questionId}/downVote")
    @ApiOperation(value = "Голосование за Question по Question id", tags = {"VoteQuestion down"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Голосование успешно произведено"),
            @ApiResponse(code = 400, message = "Вопрос с таким ID не найден или Вы уже голосовали за данный Question")
    })
    public ResponseEntity<Integer> downVote(@PathVariable Long questionId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Optional<Question> optionalQuestion = questionService.getById(questionId);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (voteQuestionService.userVoteCheck(questionId, user.getId())) {
                VoteQuestion voteQuestion = new VoteQuestion(user, question, LocalDateTime.now(), VoteType.DOWN_VOTE);
                voteQuestionService.persist(voteQuestion);
                return new ResponseEntity<>(voteQuestionService.getTotalVoteQuestionsByQuestionId(questionId),
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    @ApiOperation(value = "Получение всех QuestionDto с пагинацией", tags = {"Get All QuestionDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Все QuestionDto получены"),
            @ApiResponse(code = 400, message = "QuestionDto не найдены")
    })
    public ResponseEntity<PageDto<QuestionDto>> getAllQuestionDto(@RequestParam int currentPageNumber,
                                                                  @RequestParam(defaultValue = "10") int itemsOnPage,
                                                                  @RequestParam(required = false) List<Long> trackedTags,
                                                                  @RequestParam(required = false) List<Long> ignoredTags) {
        Map<String, Object> paginationMap = new HashMap<>();
        paginationMap.put("class", "AllQuestionDto");
        paginationMap.put("currentPageNumber", currentPageNumber);
        paginationMap.put("itemsOnPage", itemsOnPage);
        paginationMap.put("trackedTags", trackedTags);
        paginationMap.put("ignoredTags", ignoredTags);

        return ResponseEntity.ok(questionDtoService.getPageDto(currentPageNumber, itemsOnPage, paginationMap));
    }

    @GetMapping("tag/{id}")
    @ApiOperation(value = "Получение QuestionDto по TagId", tags = {"Получение QuestionDto по tagId"})
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "QuestionDto успешно получено"),
            @ApiResponse(code = 400, message = "Данный TagId не найден")
    })
    public ResponseEntity<?> getQuestionDtoByTagId(@PathVariable Long id,
                                                   @RequestParam int page,
                                                   @RequestParam(defaultValue="10") int items) {
        if (tagService.existsById(id)) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("class", "AllQuestionDtoByTagId");
            objectMap.put("tagId", id);
            objectMap.put("currentPageNumber", page);
            objectMap.put("itemsOnPage", items);
            PageDto<QuestionDto> pageDto = questionDtoService.getPageDto(page, items, objectMap);
            return new ResponseEntity<>(pageDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("TagId " + id + " not found", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/count")
    @ApiOperation(value = "Получение количества вопросов в Question", tags = {"QuestionCount"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Количество вопросов успешно получено")
    })
    public ResponseEntity<Integer> getCountQuestion() {
        return new ResponseEntity<>(questionService.getCountQuestion(), HttpStatus.OK);
    }
}
