package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.webapp.controllers.dto.AuthenticationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class QuestionResourceControllerTest extends AbstractApiTest {

    @PersistenceContext
    EntityManager entityManager;

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
                Assertions.assertNotNull(entityManager.createQuery("FROM VoteQuestion a WHERE a.question.id =:questionId and a.user.id =: userId", VoteQuestion.class)
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
        Assertions.assertNotNull(entityManager.createQuery("FROM VoteQuestion a WHERE a.question.id =:questionId and a.user.id =: userId", VoteQuestion.class)
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

    public String getJwtToken(String email, String password) throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);
        String json = objectMapper.writeValueAsString(request);
        MvcResult m = this.mvc.perform(post("/api/auth/token").contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andReturn();
        return "Bearer " + m.getResponse().getContentAsString();
    }
}