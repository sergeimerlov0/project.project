package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceAnswerControllerTest extends AbstractApiTest {
    @Autowired
    private AnswerService answerService;

    @Test
    @DataSet("src/test/resources/datasets/answerTest/answer_delete_data_set.xml")
    void deleteAnswerById() {
        answerService.deleteById(100L);
        assertEquals(1L, answerService.getAll().size());
    }
}