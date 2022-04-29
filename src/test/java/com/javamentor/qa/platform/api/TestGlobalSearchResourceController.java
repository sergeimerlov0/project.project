package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

        this.mvc.perform(MockMvcRequestBuilders.get("/api/search/?q=created:2015&page=1&items=10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());
    }

}