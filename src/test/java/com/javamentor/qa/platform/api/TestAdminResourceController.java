package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAdminResourceController extends AbstractApiTest {
    @Test
    @DataSet(value = {
            "datasets/AdminResourceController/Delete/user.yml",
            "datasets/AdminResourceController/Delete/role.yml"
    })
    void delete() throws Exception {
        //Проверяем, что с ролью USER недоступно
        this.mvc.perform(get("/api/admin/delete/1user@mail.ru")
                        .header("Authorization", getJwtToken("2user@mail.ru", "123")))
                .andExpect(status().isForbidden());

        //В датасетах User c id 101 имеет поле IsEnabled=true
        this.mvc.perform(post("/api/admin/delete/1user@mail.ru")
                        .header("Authorization", getJwtToken("4user@mail.ru", "3111")))
                .andExpect(status().isOk());

        //Проверяем повторную попытку удалить
        this.mvc.perform(post("/api/admin/delete/1user@mail.ru")
                        .header("Authorization", getJwtToken("4user@mail.ru", "3111")))
                .andExpect(status().isBadRequest());

        //Проверяем, что User c id 101 отключен
        this.mvc.perform(get("/api/user/101")
                        .header("Authorization", getJwtToken("2user@mail.ru", "123")))
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {
            "datasets/AdminResourceController/getListDeletedAnswer/answer.yml",
            "datasets/AdminResourceController/getListDeletedAnswer/question.yml",
            "datasets/AdminResourceController/getListDeletedAnswer/reputation.yml",
            "datasets/AdminResourceController/getListDeletedAnswer/role.yml",
            "datasets/AdminResourceController/getListDeletedAnswer/user.yml",
            "datasets/AdminResourceController/getListDeletedAnswer/voteAnswer.yml"
    })
    void getListDeletedAnswer() throws Exception {
        //Проверяем отсутствие удалённых ответов у user с id = 101
        this.mvc.perform(MockMvcRequestBuilders.get("/api/admin/answer/delete?userId=101")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        //Проверяем наличие одного удалённого ответа у user с id = 102
        this.mvc.perform(MockMvcRequestBuilders.get("/api/admin/answer/delete?userId=102")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(103)))
                .andExpect(jsonPath("$[0].userId", is(102)));

        //Проверяем наличие двух удалённых ответов у user с id = 100
        this.mvc.perform(MockMvcRequestBuilders.get("/api/admin/answer/delete?userId=100")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].userId", is(100)))
                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].userId", is(100)));
    }


    @Test
    @DataSet(value = {
            "datasets/AdminResourceController/deleteAnswerById/user.yml",
            "datasets/AdminResourceController/deleteAnswerById/role.yml",
            "datasets/AdminResourceController/deleteAnswerById/answer.yml",
            "datasets/AdminResourceController/deleteAnswerById/question.yml",
            "datasets/AdminResourceController/deleteAnswerById/questionHasTag.yml",
            "datasets/AdminResourceController/deleteAnswerById/tag.yml",
            "datasets/AdminResourceController/deleteAnswerById/voteAnswer.yml",
            "datasets/AdminResourceController/deleteAnswerById/reputation.yml",

    })
    void deleteAnswerById() throws Exception {

        //Проверка, что ответ существует и удален методом
        this.mvc.perform(MockMvcRequestBuilders.delete("/api/admin/answer/100/delete")
                        .header("Authorization", getJwtToken("test2@test.ru", "123")))
                .andExpect(status().isOk())
                .andExpect(content().string("Answer is successfully deleted"));
        Assertions.assertEquals(true, em.createQuery("SELECT a.isDeleted FROM Answer a WHERE a.id = 100").getSingleResult());
        Assertions.assertEquals(true, em.createQuery("SELECT a.isDeletedByModerator FROM Answer a WHERE a.id = 100").getSingleResult());


        //Проверка, что ответа не существует
        this.mvc.perform(MockMvcRequestBuilders.delete("/api/admin/answer/103/delete")
                        .header("Authorization", getJwtToken("test2@test.ru", "123")))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Answer is not exist"));

        //Проверка, что  пользователь не обладает правами
        this.mvc.perform(MockMvcRequestBuilders.delete("/api/admin/answer/103/delete")
                        .header("Authorization", getJwtToken("test3@test.ru", "123")))
                .andExpect(status().isForbidden());

    }
}