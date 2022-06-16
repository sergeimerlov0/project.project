package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestChatResourceController extends AbstractApiTest {

    private String email;
    private String password;

    @Test
    @DataSet(value = {
            "datasets/ChatResourceController/getPaginationMessageSortedDate/chat.yml",
            "datasets/ChatResourceController/getPaginationMessageSortedDate/message.yml",
            "datasets/ChatResourceController/getPaginationMessageSortedDate/user.yml",
            "datasets/ChatResourceController/getPaginationMessageSortedDate/role.yml"}
            , cleanBefore = true, cleanAfter = true)
    void getPaginationMessageSortedDate() throws Exception{

        email = "3user@mail.ru";
        password = "3111";

        mvc.perform(get("/api/user/chat/100/single/message")
                        .header("Authorization", getJwtToken(email, password))
                        .param("currentPageNumber", "1")
                        .param("itemsOnPage", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].message", is("test100")))
                .andExpect(jsonPath("$[0].nickName", is("user100")))
                .andExpect(jsonPath("$[0].userId", is(100)))
                .andExpect(jsonPath("$[0].image", is("image100")))
                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].message", is("test101")))
                .andExpect(jsonPath("$[1].nickName", is("user101")))
                .andExpect(jsonPath("$[1].userId", is(101)))
                .andExpect(jsonPath("$[1].image", is("image101")))
                .andExpect(jsonPath("$[2].id", is(102)))
                .andExpect(jsonPath("$[2].message", is("test102")))
                .andExpect(jsonPath("$[2].nickName", is("user102")))
                .andExpect(jsonPath("$[2].userId", is(102)))
                .andExpect(jsonPath("$[2].image", is("image102")));
    }
}