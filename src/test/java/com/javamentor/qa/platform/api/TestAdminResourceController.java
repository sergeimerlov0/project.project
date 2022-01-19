package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAdminResourceController extends AbstractApiTest {

    @Test
    @DataSet(value = {
            "datasets/AdminResourceController/user.yml",
            "datasets/AdminResourceController/role.yml"
    })
    void setIsEnabled() throws Exception {

        //В датасетах User c id 101 имеет поле IsEnabled=true
        this.mvc.perform(post("/api/user/isEnabled/101")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

        //Проверяем, что User c id 101 отключен
        this.mvc.perform(get("/api/user/101")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isNotFound());

        //В датасетах User c id 102 имеет поле IsEnabled=false
        this.mvc.perform(post("/api/user/isEnabled/102")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

        //Проверяем, что User c id 102 включен
        this.mvc.perform(get("/api/user/102")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());
    }
}
