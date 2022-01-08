package com.javamentor.qa.platform.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.webapp.controllers.dto.AuthenticationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DataSet(value = {"datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/answer.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/question.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/questionHasTag.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/reputation.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/role.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/tag.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/user.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/voteQuestion.yml",})
    void getQuestionDtoByTagId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("3user@mail.ru");
        request.setPassword("3111");
        String json = mapper.writeValueAsString(request);
        String token = this.mvc.perform(post("/api/auth/token").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //проверяем возвращаемое значение. В датасетах 3 вопроса с TagId 100, но один из них с IsDeleted=true
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/tag/100?page=1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2)) //
//                .andExpect(jsonPath("$.items").value(6L))
                .andExpect(jsonPath("$.itemsOnPage").value(10));
    }
}