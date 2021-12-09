package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResourceAnswerControllerTest extends AbstractApiTest {

    @Test
    @DataSet("datasets/answerDatasets/answerDeleteDataSet.yml")
    void deleteAnswerById() throws Exception {
        this.mvc.perform(delete("/api/user/question/100/answer/100"))
                .andExpect(status().isOk());
    }
}