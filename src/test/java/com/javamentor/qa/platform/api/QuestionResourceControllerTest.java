package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class QuestionResourceControllerTest extends AbstractApiTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private QuestionService questionService;

    @Test
    @DataSet(value = {
            "datasets/questionDatasets/answer.yml",
            "datasets/questionDatasets/tag.yml",
            "datasets/questionDatasets/user.yml",
            "datasets/questionDatasets/role.yml",
            "datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/questionHasTag.yml",
            "datasets/questionDatasets/reputation.yml",
            "datasets/questionDatasets/voteQuestion.yml"
    })
    void getQuestionDtoById() throws Exception {
        this.mvc.perform(get("/api/user/question/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.countAnswer").value(3L))
                .andExpect(jsonPath("$.authorReputation").value(6L))
                .andExpect(jsonPath("$.countValuable").value(1L));
    }

    @Test
    @DataSet(value = {
            "datasets/questionDatasets/answer.yml",
            "datasets/questionDatasets/tag.yml",
            "datasets/questionDatasets/user.yml",
            "datasets/questionDatasets/role.yml",
            "datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/questionHasTag.yml",
            "datasets/questionDatasets/reputation.yml",
            "datasets/questionDatasets/voteQuestion.yml"
    })
    void getNonExistedQuestionDtoById() throws Exception {
        this.mvc.perform(get("/api/user/question/101"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/user.yml",
            "datasets/questionDatasets/role.yml"})
    void postUpVoteQuestion() throws Exception {
        this.mvc.perform(post("/api/user/question/100/upVote"))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertNotNull(em.createQuery("FROM VoteQuestion a WHERE a.question.id =:questionId and a.user.id =: userId", VoteQuestion.class)
                .setParameter("questionId", 100L)
                .setParameter("userId", 100L));
    }

    @Test
    @DataSet(value = {"datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/user.yml",
            "datasets/questionDatasets/role.yml"})
    void postDownVoteQuestion() throws Exception {
        this.mvc.perform(post("/api/user/question/101/downVote"))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertNotNull(em.createQuery("FROM VoteQuestion a WHERE a.question.id =:questionId and a.user.id =: userId", VoteQuestion.class)
                .setParameter("questionId", 101L)
                .setParameter("userId", 101L));
    }

    @Test
    @DataSet(value = {"datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/user.yml",
            "datasets/questionDatasets/role.yml"})
    void postUpVoteQuestionByNullQuestion() throws Exception {
        this.mvc.perform(post("/api/user/question/102/upVote"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/user.yml",
            "datasets/questionDatasets/role.yml"})
    void downUpVoteQuestionByNullQuestion() throws Exception {
        this.mvc.perform(post("/api/user/question/102/downVote"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {
            "datasets/questionDatasets/comment.yml",
            "datasets/questionDatasets/commentQuestion.yml",
            "datasets/questionDatasets/answer.yml",
            "datasets/questionDatasets/tag.yml",
            "datasets/questionCommentDatasets/user.yml",
            "datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/questionHasTag.yml",
            "datasets/questionDatasets/reputation.yml"
    })
    public void getAllQuestionCommentByQuestionId() throws Exception {
        this.mvc.perform(get("/api/user/question/100/comment")
                        .header("Authorization", getJwtToken("test@mail.ru", "password")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].questionId", is(100)))
                .andExpect(jsonPath("$[0].text", is("some text 1")))
                .andExpect(jsonPath("$[0].userId", is(100)))
                .andExpect(jsonPath("$[0].reputation", is(6)))
                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].questionId", is(100)))
                .andExpect(jsonPath("$[1].text", is("some text 2")))
                .andExpect(jsonPath("$[1].userId", is(101)));
    }

    @Test
    @DataSet(value = {
            "datasets/questionDatasets/comment.yml",
            "datasets/questionDatasets/commentQuestion.yml",
            "datasets/questionDatasets/answer.yml",
            "datasets/questionDatasets/tag.yml",
            "datasets/questionCommentDatasets/user.yml",
            "datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/questionHasTag.yml",
            "datasets/questionDatasets/reputation.yml"
    })
    public void getEmptyListQuestionCommentByQuestionId() throws Exception {
        mvc.perform(get("/api/user/question/101/comment")
                        .header("Authorization", getJwtToken("test@mail.ru", "password")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DataSet(value = {
            "datasets/questionDatasets/comment.yml",
            "datasets/questionDatasets/commentQuestion.yml",
            "datasets/questionDatasets/answer.yml",
            "datasets/questionDatasets/tag.yml",
            "datasets/questionCommentDatasets/user.yml",
            "datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/questionHasTag.yml",
            "datasets/questionDatasets/reputation.yml"
    })
    public void shouldNotGetQuestionCommentByQuestionId() throws Exception {
        mvc.perform(get("/api/user/question/500/comment")
                        .header("Authorization", getJwtToken("test@mail.ru", "password")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Question with id 500 not found!"));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/role.yml",
            "datasets/QuestionResourceController/user.yml",
            "datasets/QuestionResourceController/tag.yml"
    })
    public void testCreateQuestionWithEmptyOrNullTitle() throws Exception {
        List<TagDto> tags = generateTags(3);

        QuestionCreateDto questionCreateDtoEmptyTitle = new QuestionCreateDto(
                "",
                "description",
                tags
        );

        QuestionCreateDto questionCreateDtoNullTitle = new QuestionCreateDto(
                null,
                "description",
                tags
        );

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDtoEmptyTitle))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException, "should be MethodArgumentNotValidException exception"));

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDtoNullTitle))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException, "should be MethodArgumentNotValidException exception"));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/role.yml",
            "datasets/QuestionResourceController/user.yml",
            "datasets/QuestionResourceController/tag.yml"
    })
    public void testCreateQuestionWithEmptyOrNullDescription() throws Exception {
        List<TagDto> tags = generateTags(3);

        QuestionCreateDto questionCreateDtoEmptyDescription = new QuestionCreateDto(
                "title",
                "",
                tags
        );

        QuestionCreateDto questionCreateDtoNullDescription = new QuestionCreateDto(
                "title",
                null,
                tags
        );

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDtoEmptyDescription))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException, "should be MethodArgumentNotValidException exception"));

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDtoNullDescription))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException, "should be MethodArgumentNotValidException exception"));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/role.yml",
            "datasets/QuestionResourceController/user.yml",
            "datasets/QuestionResourceController/tag.yml"
    })
    public void testCreateQuestionWithEmptyOrNullTags() throws Exception {
        QuestionCreateDto questionCreateDtoEmptyTags = new QuestionCreateDto(
                "title",
                "description",
                List.of()
        );

        QuestionCreateDto questionCreateDtoNullTags = new QuestionCreateDto(
                "title",
                "description",
                null
        );

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDtoEmptyTags))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException, "should be MethodArgumentNotValidException exception"));

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDtoNullTags))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException, "should be MethodArgumentNotValidException exception"));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/role.yml",
            "datasets/QuestionResourceController/user.yml",
            "datasets/QuestionResourceController/tag.yml"
    })
    public void testCreateQuestionWithNewTags() throws Exception {
        List<TagDto> tags = generateTags(3);
        QuestionCreateDto questionCreateDto = new QuestionCreateDto(
                "title",
                "description",
                tags
        );

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isOk());

        long expectedCount = 10L + tags.size();
        assertEquals(expectedCount, (long) em.createQuery("select count(t) from Tag t").getSingleResult(), "tag table should have" + expectedCount + " rows");
        tags.forEach(tag -> assertTrue(tagService.getByName(tag.getName()).isPresent(), "there is no " + tag.getName() + " in tag table"));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/role.yml",
            "datasets/QuestionResourceController/user.yml",
            "datasets/QuestionResourceController/tag.yml"
    })
    public void testCreateQuestionWithExistedTags() throws Exception {
        TagDto existedTag1 = new TagDto(null, "tag104", null);
        TagDto existedTag2 = new TagDto(null, "tag105", null);

        QuestionCreateDto questionCreateDto = new QuestionCreateDto(
                "title",
                "description",
                List.of(existedTag1, existedTag2)
        );

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isOk());

        long expectedCount = 10L;
        assertEquals(expectedCount, (long) em.createQuery("select count(t) from Tag t").getSingleResult(), "tag table should have" + expectedCount + " rows");
        assertTrue(tagService.getByName(existedTag1.getName()).isPresent(), "there is no " + existedTag1.getName() + " in tag table");
        assertTrue(tagService.getByName(existedTag2.getName()).isPresent(), "there is no " + existedTag2.getName() + " in tag table");
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/role.yml",
            "datasets/QuestionResourceController/user.yml",
            "datasets/QuestionResourceController/tag.yml"
    })
    public void testCreateQuestionPresentInDb() throws Exception {
        List<TagDto> tags = generateTags(3);
        QuestionCreateDto questionCreateDto = new QuestionCreateDto(
                "title",
                "description",
                tags
        );

        MvcResult result = mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isOk())
                .andReturn();

        Long id = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id", Long.class);
        assertTrue(questionService.getById(id).isPresent(), "there's no question in db after creation");
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/role.yml",
            "datasets/QuestionResourceController/user.yml",
            "datasets/QuestionResourceController/tag.yml"
    })
    public void testCreateQuestionByAdmin() throws Exception {
        List<TagDto> tags = generateTags(3);
        QuestionCreateDto questionCreateDto = new QuestionCreateDto(
                "title",
                "description",
                tags
        );

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("admin@gmail.com", "123")))
                .andExpect(status().isForbidden());
    }


    private List<TagDto> generateTags(int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj((n) -> new TagDto(null, "tag #" + n, null))
                .collect(Collectors.toList());
    }
}