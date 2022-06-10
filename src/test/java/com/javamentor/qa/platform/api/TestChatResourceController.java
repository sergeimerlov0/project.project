package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestChatResourceController extends AbstractApiTest {

    @Test
    @DataSet(value = {
            "datasets/UserResourceController/getUserByReg/role.yml",
            "datasets/UserResourceController/getUserByReg/users.yml"},
            cleanBefore = true, cleanAfter = true)
    void getUserByReg() throws Exception {
        //email и пароль админа
        String email = "admin@mail.ru";
        String password = "password";

        //пытаемся получить дто через админа
        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken(email, password))
                        .param("currentPageNumber", "1")
                        .param("itemsOnPage", "1")
                )
                .andDo(print())
                .andExpect(status().isForbidden());

        //email и пароль корректного юзера
        email = "test@mail.ru";
        password = "password";

        //проверяем что вернется 1 user на первой странице по дате регистрации
        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken(email, password))
                        .param("currentPageNumber", "1")
                        .param("itemsOnPage", "1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].id").value(104));

        //проверяем что вернутся 5 юзера на первой странице по дате регистрации, т.к 1 удален
        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken(email, password))
                        .param("currentPageNumber", "1")
                        .param("itemsOnPage", "5")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(5))
                .andExpect(jsonPath("$.items[0].id").value(104))
                .andExpect(jsonPath("$.items[1].id").value(103))
                .andExpect(jsonPath("$.items[2].id").value(102))
                .andExpect(jsonPath("$.items[3].id").value(101))
                .andExpect(jsonPath("$.items[4].id").value(100));

        //проверяем что вернется 1 user на 2 странице(т.е второй по дате регистрации)
        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken(email, password))
                        .param("currentPageNumber", "2")
                        .param("itemsOnPage", "1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].id").value(103));
    }

    @Test
    @DataSet(value = {
            "datasets/ChatResourceController/getPaginationMessageSortedDate/answer.yml",
            "datasets/ChatResourceController/getPaginationMessageSortedDate/question.yml",
            "datasets/ChatResourceController/getPaginationMessageSortedDate/reputation.yml",
            "datasets/ChatResourceController/getPaginationMessageSortedDate/role.yml",
            "datasets/ChatResourceController/getPaginationMessageSortedDate/user.yml",
            "datasets/ChatResourceController/getPaginationMessageSortedDate/voteAnswer.yml",
            "datasets/ChatResourceController/getPaginationMessageSortedDate/voteQuestion.yml"}
            , cleanBefore = true, cleanAfter = true)
    void getAllUserSortVote() throws Exception{
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/vote/?currentPageNumber=1")
                .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //Проверяем страницу
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(4))
                .andExpect(jsonPath("$.itemsOnPage").value(10))

                //Проверка на сортировку юзеров по голосам
                .andExpect(jsonPath("$.items[0].id").value(101))
                .andExpect(jsonPath("$.items[1].id").value(100))
                .andExpect(jsonPath("$.items[2].id").value(102))
                .andExpect(jsonPath("$.items[3].id").value(103))

                //Проверка полей items
                .andExpect(jsonPath("$.items[0].city").value("city101"))
                .andExpect(jsonPath("$.items[0].email").value("test_user101@mail.ru"))
                .andExpect(jsonPath("$.items[0].linkImage").value("image101"))
                .andExpect(jsonPath("$.items[0].reputation").value(30))
                .andExpect(jsonPath("$.items[0].fullName").value("User with id 101"));

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/vote/?currentPageNumber=2&itemsOnPage=2")
                .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(2))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(4));
    }

}