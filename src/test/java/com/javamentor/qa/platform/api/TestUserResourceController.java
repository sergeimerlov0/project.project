package com.javamentor.qa.platform.api;


import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUserResourceController extends AbstractApiTest {
    private String email;
    private String password;

    @Test
    @DataSet(value = {
            "datasets/UserResourceController/updatePasswordByEmail/users.yml",
            "datasets/UserResourceController/updatePasswordByEmail/role.yml"},
            cleanBefore = true, cleanAfter = true)
    void updatePasswordByEmail() throws Exception {
        email = "user@mail.ru";
        password = "$2a$10$jEIIl6EDnJspLm0LGNCaXOiqvJQNdqPNDydyR2tR5gzhmMx2hQ/Lq"; //Пароль в базе 1qaz2WSX$

        //проверяем что пароль изменится
        mvc.perform(MockMvcRequestBuilders.put("/api/user/100/change/password")
                        .header("Authorization", getJwtToken(email, password))
                .content("123qweASD$"))
                .andDo(print())
                .andExpect(status().isOk());

        password = "123qweASD$";
        //проверяем что пароль не соответствует требованиям
        mvc.perform(MockMvcRequestBuilders.put("/api/user/100/change/password")
                        .header("Authorization", getJwtToken(email, password))
                        .content("123qw"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //проверяем что новый пароль совпадает с текущим
        mvc.perform(MockMvcRequestBuilders.put("/api/user/100/change/password")
                        .header("Authorization", getJwtToken(email, password))
                        .content("123qweASD$"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //проверяем что ID не совпадает с текущим пользователем
        mvc.perform(MockMvcRequestBuilders.put("/api/user/101/change/password")
                        .header("Authorization", getJwtToken(email, password))
                        .content("123qweASD$"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {
            "datasets/UserResourceController/getUserById/roles.yml",
            "datasets/UserResourceController/getUserById/users.yml",
            "datasets/UserResourceController/getUserById/question.yml",
            "datasets/UserResourceController/getUserById/answer.yml",
            "datasets/UserResourceController/getUserById/reputations.yml"

    }, cleanBefore = true, cleanAfter = true)
    void getUserById() throws Exception {
        email = "user1@mail.ru";
        password = "password";

        //проверяем что вернется 1 user по ID 100
        mvc.perform(MockMvcRequestBuilders.get("/api/user/100")
                        .header("Authorization", getJwtToken(email, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.email").value("user1@mail.ru"))
                .andExpect(jsonPath("$.fullName").value("user1"))
                .andExpect(jsonPath("$.linkImage").value("user1link"))
                .andExpect(jsonPath("$.city").value("user1city"))
                .andExpect(jsonPath("$.reputation").value(2));

        //проверяем что такого юзера нет
        mvc.perform(MockMvcRequestBuilders.get("/api/user/106")
                        .header("Authorization", getJwtToken(email, password)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @DataSet(value = {
            "datasets/UserResourceController/getUserByReg/role.yml",
            "datasets/UserResourceController/getUserByReg/users.yml"},
            cleanBefore = true, cleanAfter = true)
    void getUserByReg() throws Exception {
        //email и пароль админа
        email = "admin@mail.ru";
        password = "password";

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
            "datasets/UserResourceController/getAllUserSortVote/answer.yml",
            "datasets/UserResourceController/getAllUserSortVote/question.yml",
            "datasets/UserResourceController/getAllUserSortVote/reputation.yml",
            "datasets/UserResourceController/getAllUserSortVote/role.yml",
            "datasets/UserResourceController/getAllUserSortVote/user.yml",
            "datasets/UserResourceController/getAllUserSortVote/voteAnswer.yml",
            "datasets/UserResourceController/getAllUserSortVote/voteQuestion.yml"}, cleanBefore = true, cleanAfter = true)
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

    @Test
    @DataSet(value = {
            "datasets/UserResourceController/getAllUserDtoSortReputation/answer.yml",
            "datasets/UserResourceController/getAllUserDtoSortReputation/question.yml",
            "datasets/UserResourceController/getAllUserDtoSortReputation/reputation.yml",
            "datasets/UserResourceController/getAllUserDtoSortReputation/role.yml",
            "datasets/UserResourceController/getAllUserDtoSortReputation/user.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getAllUserSortReputation() throws Exception{
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/reputation/?currentPageNumber=1")
                .header("Authorization", getJwtToken("Vasya103@mail.ru", "103")))
                .andExpect(status().isOk())

                //Проверяем страницу
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(4))
                .andExpect(jsonPath("$.itemsOnPage").value(10))

                //Проверка на сортировку юзеров по репутации
                .andExpect(jsonPath("$.items[0].id").value(102))
                .andExpect(jsonPath("$.items[1].id").value(101))
                .andExpect(jsonPath("$.items[2].id").value(100))
                .andExpect(jsonPath("$.items[3].id").value(103))

                //Проверка полей items
                .andExpect(jsonPath("$.items[1].city").value("city101"))
                .andExpect(jsonPath("$.items[1].email").value("Vasya101@mail.ru"))
                .andExpect(jsonPath("$.items[1].linkImage").value("image101"))
                .andExpect(jsonPath("$.items[1].reputation").value(3))
                .andExpect(jsonPath("$.items[1].fullName").value("Vasya 101"));

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/reputation/?currentPageNumber=2&itemsOnPage=2")
                .header("Authorization", getJwtToken("Vasya103@mail.ru", "103")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(2))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(4));
    }

    @Test
    @DataSet(value = {
            "datasets/UserResourceController/getUserByRegWithFilter/role.yml",
            "datasets/UserResourceController/getUserByRegWithFilter/users.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getUserByRegWithFilter() throws Exception {
        //email и пароль юзера
        email = "FilterTest1Kata@mail.ru";
        password = "password";

        //пытаемся получить список пользователей с фильтром "imi" без учета регистра в поле имени
        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken(email, password))
                        .param("currentPageNumber", "1")
                        .param("itemsOnPage", "3")
                        .param("filter", "ImI")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2));

        //пытаемся получить список пользователей с фильтром "test" без учета регистра в поле почты
        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken(email, password))
                        .param("currentPageNumber", "1")
                        .param("itemsOnPage", "3")
                        .param("filter", "tEsT")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2));

        //пытаемся получить список пользователей с фильтром "kata" без учета регистра в поле имени или почты
        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken(email, password))
                        .param("currentPageNumber", "1")
                        .param("itemsOnPage", "5")
                        .param("filter", "kAtA")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(3));
    }

    @Test
    @DataSet(value = {
            "datasets/UserResourceController/getAllUserSortVoteWithFilter/answer.yml",
            "datasets/UserResourceController/getAllUserSortVoteWithFilter/question.yml",
            "datasets/UserResourceController/getAllUserSortVoteWithFilter/reputation.yml",
            "datasets/UserResourceController/getAllUserSortVoteWithFilter/role.yml",
            "datasets/UserResourceController/getAllUserSortVoteWithFilter/user.yml",
            "datasets/UserResourceController/getAllUserSortVoteWithFilter/voteAnswer.yml",
            "datasets/UserResourceController/getAllUserSortVoteWithFilter/voteQuestion.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getAllUserSortVoteWithFilter() throws Exception{
        //email и пароль юзера
        email = "3user@mail.com";
        password = "3111";

        //пытаемся получить список пользователей с фильтром "test" без учета регистра в поле имени
        mvc.perform(get("/api/user/vote")
                        .header("Authorization", getJwtToken(email, password))
                        .param("currentPageNumber", "1")
                        .param("itemsOnPage", "5")
                        .param("filter", "tESt")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(3));

        //пытаемся получить список пользователей с фильтром "user" без учета регистра в поле почты
        mvc.perform(get("/api/user/vote")
                        .header("Authorization", getJwtToken(email, password))
                        .param("currentPageNumber", "1")
                        .param("itemsOnPage", "5")
                        .param("filter", "UseR")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(3));

        //пытаемся получить список пользователей с фильтром "ru" без учета регистра в поле имени или почты
        mvc.perform(get("/api/user/vote")
                        .header("Authorization", getJwtToken(email, password))
                        .param("currentPageNumber", "1")
                        .param("itemsOnPage", "5")
                        .param("filter", "rU")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2));
    }
}