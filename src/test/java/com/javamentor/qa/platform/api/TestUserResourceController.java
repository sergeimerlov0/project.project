package com.javamentor.qa.platform.api;


import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class TestUserResourceController extends AbstractApiTest {

    private String email;
    private String password;

    @Test
    @DataSet(value = {"datasets/UserResourceController/getUserByReg/role.yml","datasets/UserResourceController/getUserByReg/users.yml"
            })
    void getUserById() throws Exception {
        email = "test@mail.ru";
        password = "password";

        //проверяем что вернется 1 user
        mvc.perform(MockMvcRequestBuilders.get("/api/user/100")
                .header("Authorization", getJwtToken(email,password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.email").value("test@mail.ru"))
                .andExpect(jsonPath("$.fullName").value("Max"))
                .andExpect(jsonPath("$.linkImage").value("link"))
                .andExpect(jsonPath("$.city").value("Moscow"));

    }

    @Test
    @DataSet(value = {"datasets/UserResourceController/getUserByReg/role.yml","datasets/UserResourceController/getUserByReg/users.yml",
    })
    void shouldNotGetUserById() throws Exception {
        email = "SomeEmail1@mail.mail";
        password = "someHardPassword";
        //проверяем что такого юзера не существует
        mvc.perform(MockMvcRequestBuilders.get("/api/user/106")
                        .header("Authorization", getJwtToken(email,password)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @DataSet(value = {"datasets/UserResourceController/getUserByReg/role.yml","datasets/UserResourceController/getUserByReg/users.yml"},
            cleanBefore = true, cleanAfter = true)
    void getUserByReg() throws Exception {
        //email и пароль админа
        email = "admin@mail.ru";
        password = "password";

        //пытаемся получить дто через админа
        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken(email,password))
                        .param("page","1")
                        .param("items", "1")
                )
                .andDo(print())
                .andExpect(status().isForbidden());

        //email и пароль корректного юзера
         email = "test@mail.ru";
         password = "password";

        //проверяем что вернется 1 user на первой странице по дате регистрации
        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken(email,password))
                        .param("page","1")
                        .param("items", "1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].id").value(104));

        //проверяем что вернутся 5 юзера на первой странице по дате регистрации, т.к 1 удален
        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken(email,password))
                        .param("page","1")
                        .param("items", "5")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(5))
                .andExpect(jsonPath("$.items[0].id").value(104))
                .andExpect(jsonPath("$.items[1].id").value(103))
                .andExpect(jsonPath("$.items[2].id").value(102))
                .andExpect(jsonPath("$.items[3].id").value(100))
                .andExpect(jsonPath("$.items[4].id").value(105));
        //проверяем что вернется 1 user на 2 странице(т.е второй по дате регистрации)
        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken(email,password))
                        .param("page","2")
                        .param("items", "1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].id").value(103));
    }
}