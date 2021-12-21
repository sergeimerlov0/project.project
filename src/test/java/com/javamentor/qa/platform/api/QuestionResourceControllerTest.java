package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class QuestionResourceControllerTest extends AbstractApiTest {


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
                .andExpect(jsonPath("$.countValuable").value(2L));
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
}