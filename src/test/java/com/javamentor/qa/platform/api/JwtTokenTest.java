package com.javamentor.qa.platform.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JwtTokenTest extends AbstractApiTest {
    @Test
    @DataSet(value = {
            "datasets/JwtTokenTest/role.yml",
            "datasets/JwtTokenTest/user.yml",
    }, cleanBefore = true)
    public void checkCorrectWork() throws Exception {
        String email = "3user@mail.ru";
        String password = "3111";
        this.mvc.perform(MockMvcRequestBuilders.get("/api/check/user-status")
                        .header("Authorization", getJwtToken(email, password)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    public String getJwtToken(String email, String password) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);
        String json = mapper.writeValueAsString(request);
        MvcResult m = this.mvc.perform(post("/api/auth/token").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
        return "Bearer " + m.getResponse().getContentAsString();
    }
}