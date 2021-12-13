package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResourceAnswerControllerTest extends AbstractApiTest {

    @Autowired
    private AnswerService answerService;

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
        assertFalse(answerService.existsById(100L));
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
}