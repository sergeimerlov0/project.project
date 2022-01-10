package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
//import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@DBRider
//@SpringBootTest(classes = JmApplication.class)
//@TestPropertySource(properties = "spring.config.location = src/test/resources/application-test.properties")
//@AutoConfigureMockMvc
//@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
public class TestUserResourceController extends AbstractApiTest {

    private String email;
    private String password;

   // @Autowired
 //   private MockMvc mockMvc;

    @Test
    @DataSet(value = {"dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answer.yml",
            "dataset/UserResourceController/question.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"})
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
    @DataSet(value = {"dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answer.yml",
            "dataset/UserResourceController/question.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"})
    void shouldNotGetUserById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/user/105"))
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