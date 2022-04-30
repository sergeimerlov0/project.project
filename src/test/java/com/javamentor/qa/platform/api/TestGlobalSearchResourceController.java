package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestGlobalSearchResourceController extends AbstractApiTest {

    //    Тест работы фильтра по дате создания
    @Test
    @DataSet(value = {
            "datasets/GlobalSearchResourceController/GlobalSearchByDate/answer.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByDate/question.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByDate/questionHasTag.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByDate/reputation.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByDate/role.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByDate/tag.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByDate/user.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByDate/voteQuestion.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByDate/comment.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByDate/commentQuestion.yml"
    })
    public void searchByData() throws Exception {

//  Проверяем запись created: 2020
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:2020&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[*].id").value(103));

//  Проверяем запись created: 2020-01
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:2020-01&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[*].id").value(103));

//  Проверяем запись created: 2020-01-30
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:2020-01-30&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[*].id").value(103));

//  Проверяем запись created: 2019...2020
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:  2019...2020&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[*].id").value(103));

//  Проверяем запись created: 2019-12..2020-01
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:2019-12..2020-02&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[*].id").value(103));

//  Проверяем запись created: 2020-01-29..2020-01-30
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created: 2020-01-29..2020-01-30&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[*].id").value(103));

//  Как создавать вопросы с относительной датой я хз
//  Проверяем возвращаемый статус
//  Проверяем запись created: 1d
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:1d&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

//  Проверяем запись created:1d...
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:1d...&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

//  Проверяем запись created:1m
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:1m &page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

//  Проверяем запись created:1m...
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:1m...&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

//  Проверяем запись created: 1y
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:1y&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

//  Проверяем запись created: 1y...
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:1y...&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

//  Проверяем запись created: 1y...4m
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:1y...4m&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());
    }
}