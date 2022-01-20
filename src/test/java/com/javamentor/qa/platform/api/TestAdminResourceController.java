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
    void delete() throws Exception {

        //Проверяем, что с ролью USER недоступно
        this.mvc.perform(get("/api/admin/delete/1user@mail.ru")
                        .header("Authorization", getJwtToken("2user@mail.ru", "123")))
                .andExpect(status().isForbidden());

        //В датасетах User c id 101 имеет поле IsEnabled=true
        this.mvc.perform(post("/api/admin/delete/1user@mail.ru")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

        //Проверяем, что User c id 101 отключен
        this.mvc.perform(get("/api/user/101")
                        .header("Authorization", getJwtToken("2user@mail.ru", "123")))
                .andExpect(status().isNotFound());
    }
}
