package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnswerResourceControllerTest extends AbstractApiTest {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DataSet(value = {
            "datasets/answerDatasets/answer.yml",
            "datasets/answerDatasets/tag.yml",
            "datasets/answerDatasets/user.yml",
            "datasets/answerDatasets/role.yml",
            "datasets/answerDatasets/question.yml",
            "datasets/answerDatasets/questionHasTag.yml"
    })
    void deleteAnswerById() throws Exception {
        this.mvc.perform(delete("/api/user/question/100/answer/100"))
                .andExpect(status().isOk());
        Assertions.assertFalse(existsById(100L));
    }

    @Test
    @DataSet(value = {
            "datasets/answerDatasets/answer.yml",
            "datasets/answerDatasets/tag.yml",
            "datasets/answerDatasets/user.yml",
            "datasets/answerDatasets/role.yml",
            "datasets/answerDatasets/question.yml",
            "datasets/answerDatasets/questionHasTag.yml"
    })
    void tryToDeleteNonExistedId() throws Exception {
        this.mvc.perform(delete("/api/user/question/100/answer/104"))
                .andExpect(status().isBadRequest());
    }

    public boolean existsById(Long id) {
        long count = (long) entityManager.createQuery("SELECT COUNT(e) FROM " + Answer.class.getName() +
                        " e WHERE e.id =: id")
                .setParameter("id", id).getSingleResult();
        return count > 0;
    }

    @Test
    @DataSet(value = {
            "getAnswerDataSet/answer.yml",
            "getAnswerDataSet/question.yml",
            "getAnswerDataSet/questionHasTag.yml",
            "getAnswerDataSet/tag.yml",
            "getAnswerDataSet/reputation.yml",
            "getAnswerDataSet/role.yml",
            "getAnswerDataSet/user.yml",
            "getAnswerDataSet/voteAnswer.yml"
    })
    public void getAnswerByQuestionId() throws Exception {

        this.mvc.perform(get("/api/user/question/100/answer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].userReputation", is(23)))
                .andExpect(jsonPath("$[0].countValuable", is(2)))
                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].userReputation", is(106)))
                .andExpect(jsonPath("$[1].countValuable", is(-2)));
    }

    @Test
    @DataSet(value = {
            "getAnswerDataSet/answer.yml",
            "getAnswerDataSet/question.yml",
            "getAnswerDataSet/questionHasTag.yml",
            "getAnswerDataSet/tag.yml",
            "getAnswerDataSet/reputation.yml",
            "getAnswerDataSet/role.yml",
            "getAnswerDataSet/user.yml",
            "getAnswerDataSet/voteAnswer.yml"
    })
    public void getEmptyListAnswerByQuestionId() throws Exception {

        this.mvc.perform(get("/api/user/question/2000/answer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

    }

    @Test
    @DataSet(value = {
            "datasets/AnswerResourceController/votingApiDatasets/answer.yml",
            "datasets/AnswerResourceController/votingApiDatasets/question.yml",
            "datasets/AnswerResourceController/votingApiDatasets/questionHasTag.yml",
            "datasets/AnswerResourceController/votingApiDatasets/tag.yml",
            "datasets/AnswerResourceController/votingApiDatasets/reputation.yml",
            "datasets/AnswerResourceController/votingApiDatasets/role.yml",
            "datasets/AnswerResourceController/votingApiDatasets/user.yml",
            "datasets/AnswerResourceController/votingApiDatasets/voteAnswer.yml"
    })
    public void setUpVoteAnswerByAnswerId() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzMTExIiwianRpIjoiM3VzZXJAbWFpbC5ydSJ9.TI4PpDRYsIAnufhQufAJNDcFuQb09d5-IMBSdHZtA5M";
        //todo переделать после исправления получения токена в тестах

        //проверяем возвращаемое значение. В датасетах в базе данных уже было 2 голоса ЗА ответ с id 100
        this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/upVote")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));


        //Проверяем, что в БД появилась запись о голосовании от пользователя с id 100 (наш авторизованный юзер) по ответу с id 100
        Assertions.assertTrue(entityManager.createQuery("select v.vote from VoteAnswer v where v.user.id=:user and v.answer.id=:answer")
                .setParameter("user", 100L)
                .setParameter("answer", 100L)
                .getSingleResult()
                .toString()
                .contentEquals("UP_VOTE"));

        //проверяем невозможность проголосовать дважды за один ответ, как за, так и против
        Assertions.assertTrue(this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/upVote")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().contains("ConstraintViolationException"));

        Assertions.assertTrue(this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/downVote")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().contains("ConstraintViolationException"));
    }

    @Test
    @DataSet(value = {
            "datasets/AnswerResourceController/votingApiDatasets/answer.yml",
            "datasets/AnswerResourceController/votingApiDatasets/question.yml",
            "datasets/AnswerResourceController/votingApiDatasets/questionHasTag.yml",
            "datasets/AnswerResourceController/votingApiDatasets/tag.yml",
            "datasets/AnswerResourceController/votingApiDatasets/reputation.yml",
            "datasets/AnswerResourceController/votingApiDatasets/role.yml",
            "datasets/AnswerResourceController/votingApiDatasets/user.yml",
            "datasets/AnswerResourceController/votingApiDatasets/voteAnswer.yml"
    })
    public void setDownVoteAnswerByAnswerId() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzMTExIiwianRpIjoiM3VzZXJAbWFpbC5ydSJ9.TI4PpDRYsIAnufhQufAJNDcFuQb09d5-IMBSdHZtA5M";
        //todo переделать после исправления получения токена в тестах

        //проверяем возвращаемое значение. В датасетах в базе данных уже было 2 голоса ЗА ответ с id 100
        this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/downVote")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));


        //Проверяем, что в БД появилась запись о голосовании от пользователя с id 100 (наш авторизованный юзер) по ответу с id 100
        Assertions.assertTrue(entityManager.createQuery("select v.vote from VoteAnswer v where v.user.id=:user and v.answer.id=:answer")
                .setParameter("user", 100L)
                .setParameter("answer", 100L)
                .getSingleResult()
                .toString()
                .contentEquals("DOWN_VOTE"));

        //проверяем невозможность проголосовать дважды за один ответ, как за, так и против
        Assertions.assertTrue(this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/upVote")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().contains("ConstraintViolationException"));

        Assertions.assertTrue(this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/downVote")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().contains("ConstraintViolationException"));
    }
}