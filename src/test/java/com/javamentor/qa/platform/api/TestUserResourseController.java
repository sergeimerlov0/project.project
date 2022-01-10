package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUserResourseController extends AbstractApiTest {

    @Test
    @DataSet(value = {"datasets/UserResourceController/users.yml",
            "datasets/UserResourceController/answer.yml",
            "datasets/UserResourceController/question.yml",
            "datasets/UserResourceController/reputations.yml",
            "datasets/UserResourceController/roles.yml"})
    void getUserById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/user/101"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.email").value("SomeEmail@mail.mail"))
                .andExpect(jsonPath("$.fullName").value("Max"))
                .andExpect(jsonPath("$.linkImage").value("link"))
                .andExpect(jsonPath("$.city").value("Moscow"))
                .andExpect(jsonPath("$.reputation").value(101));
    }

    @Test
    @DataSet(value = {"datasets/UserResourceController/users.yml",
            "datasets/UserResourceController/answer.yml",
            "datasets/UserResourceController/question.yml",
            "datasets/UserResourceController/reputations.yml",
            "datasets/UserResourceController/roles.yml"})
    void shouldNotGetUserById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/user/105"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }
}