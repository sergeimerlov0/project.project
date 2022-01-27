package com.javamentor.qa.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DBRider
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource(properties = {"spring.config.location = src/test/resources/application-test.properties"})
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
@AutoConfigureMockMvc
public class AbstractApiTest {

    @Autowired
    public MockMvc mvc;
    @PersistenceContext
    protected EntityManager em;
    @Autowired
    protected ObjectMapper objectMapper;

    public String getJwtToken(String email, String password) throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);
        String json = objectMapper.writeValueAsString(request);
        MvcResult m = this.mvc.perform(post("/api/auth/token").contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andReturn();
        return "Bearer " + m.getResponse().getContentAsString();
    }
}
