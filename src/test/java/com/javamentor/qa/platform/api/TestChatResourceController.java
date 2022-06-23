package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    @Test
    @DataSet(value = {
            "datasets/ChatResourceController/getPaginationMessageSortedDate/singleChat/chat.yml",
            "datasets/ChatResourceController/getPaginationMessageSortedDate/singleChat/message.yml",
            "datasets/ChatResourceController/getPaginationMessageSortedDate/singleChat/user.yml",
            "datasets/ChatResourceController/getPaginationMessageSortedDate/singleChat/role.yml"}
            , cleanBefore = true, cleanAfter = true)
    void getAllOfSingleChatDto() throws Exception{

        email = "3user@mail.ru";
        password = "3111";

        mvc.perform(get("/api/user/chat/single")
                        .header("Authorization", getJwtToken(email, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].name", is("user100")))
                .andExpect(jsonPath("$[0].image", is("image100")))
                .andExpect(jsonPath("$[0].lastMessage", is("test100")))
                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].name", is("user101")))
                .andExpect(jsonPath("$[1].image", is("image101")))
                .andExpect(jsonPath("$[1].lastMessage", is("test101")));
    }
    @Test
    @DataSet(value = {
            "datasets/ChatResourceController/groupChat/user_entity.yml",
            "datasets/ChatResourceController/groupChat/role.yml",
            "datasets/ChatResourceController/groupChat/chat.yml",
            "datasets/ChatResourceController/groupChat/group_chat.yml",
            "datasets/ChatResourceController/groupChat/message.yml",
            "datasets/ChatResourceController/groupChat/groupchat_has_users.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getGroupChat() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                        .get("/api/user/chat/group?itemsOnPage=30&currentPageNumber=1&chatId=100")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isNotEmpty())

                //Проверяем chatId = 100 и его имя = "white"
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.chatName", is("white")))

                //Проверяем, что первое сообщение - самое свежеполученное (отправлено позже всех)
                .andExpect(jsonPath("$.pageOfMessageDto.items[0].userId", is(104)))
                .andExpect(jsonPath("$.pageOfMessageDto.items[0].id", is(106)))
                //Проверяем порядок остальных сообщений:
                .andExpect(jsonPath("$.pageOfMessageDto.items[1].id", is(105)))
                .andExpect(jsonPath("$.pageOfMessageDto.items[2].id", is(104)))
                .andExpect(jsonPath("$.pageOfMessageDto.items[3].id", is(103)))
                .andExpect(jsonPath("$.pageOfMessageDto.items[4].id", is(102)))
                .andExpect(jsonPath("$.pageOfMessageDto.items[5].id", is(101)))
                .andExpect(jsonPath("$.pageOfMessageDto.items[6].id", is(100)))
                //Проверяем, что 6е по времени отправки сообщение "hi"
                .andExpect(jsonPath("$.pageOfMessageDto.items[6].message", is("hi")));


        this.mvc.perform(MockMvcRequestBuilders
                        .get("/api/user/chat/group?itemsOnPage=30&currentPageNumber=1&chatId=101")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isNotEmpty())
                //Проверяем chatId = 101 и его имя = "blue"
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(jsonPath("$.chatName", is("blue")))
                //Проверяем его сообщение
                .andExpect(jsonPath("$.pageOfMessageDto.items[0].userId", is(104)))
                .andExpect(jsonPath("$.pageOfMessageDto.items[0].id", is(107)))

                //Проверяем, что  "hi"
                .andExpect(jsonPath("$.pageOfMessageDto.items[0].message", is("hello")));
    }
}