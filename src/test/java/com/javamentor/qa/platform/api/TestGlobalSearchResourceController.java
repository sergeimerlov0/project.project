package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestGlobalSearchResourceController extends AbstractApiTest {

    //    Тест работы фильтра по оценке
    @Test
    @DataSet(value = {
            "datasets/GlobalSearchResourceController/GlobalSearchByScore/answer.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByScore/question.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByScore/questionHasTag.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByScore/reputation.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByScore/role.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByScore/tag.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByScore/user.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByScore/voteQuestion.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByScore/comment.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByScore/commentQuestion.yml",
            "datasets/GlobalSearchResourceController/GlobalSearchByScore/bookmarks.yml"
    })
    public void searchByScore() throws Exception {

//  Проверяем запись score:1
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=score:1 &page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[*].id").value(100));

//  Проверяем запись score: ..2
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=score: ..2&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[*].id").value(100));

//  Проверяем запись score:4...
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=score: 4...&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[*].id").value(103));

//  Проверяем запись score:2...4
        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=score:2...4&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[*].id").value(102));

    }
}