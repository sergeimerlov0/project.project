package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}