package com.javamentor.qa.platform;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JwtTokenTest extends AbstractApiTest {

    @Test
    public void JwtProcessing() throws Exception {
        this.mvc.perform(post("/api/auth/token").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"123@mail.ru\",\"password\":\"123\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void forbidden() throws Exception {
        this.mvc.perform(get("/getall").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden());
    }
}
