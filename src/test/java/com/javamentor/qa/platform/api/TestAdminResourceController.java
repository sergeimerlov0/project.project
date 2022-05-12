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
}