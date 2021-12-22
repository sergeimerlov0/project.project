package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ResourceAnswerControllerTest extends AbstractApiTest {

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
        public void getAnswerByQuestionId() throws Exception{

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
    public void getEmptyListAnswerByQuestionId() throws Exception{

        this.mvc.perform(get("/api/user/question/2000/answer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

    }

}