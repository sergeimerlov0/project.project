package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/question")
@Api(value = "Работа с вопросами", tags = {"Question"})
public class QuestionResourceController {
    private final QuestionDtoService questionDtoService;
    private final QuestionService questionService;
    private final TagDtoService tagDtoService;
    private final TagService tagService;

    @GetMapping("{id}")
    @ApiOperation(value = "Get QuestionDto by question id", tags = {"QuestionDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "QuestionDto успешно получено"),
            @ApiResponse(code = 400, message = "Вопрос с таким ID не найден")
    })
    public ResponseEntity<?> getQuestionDtoById(@PathVariable Long id) {
        System.out.println("Tester");
        return questionDtoService.getQuestionDtoByQuestionId(id).isEmpty() ?
                new ResponseEntity<>("Question with id " + id + " not found!", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(questionDtoService.getQuestionDtoByQuestionId(id), HttpStatus.OK);
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
}

