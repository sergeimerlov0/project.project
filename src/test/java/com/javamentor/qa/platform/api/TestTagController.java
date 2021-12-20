package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestTagController extends AbstractApiTest {

    @Autowired
    UserService userService;

    @Test
    @DataSet(value = {
            "datasets/tagDatasets/role.yml",
            "datasets/tagDatasets/user.yml",
            "datasets/tagDatasets/tag.yml",
            "datasets/tagDatasets/tagIgnore.yml"
    })
    @WithMockUser (username = "123@mail.com", authorities = "ADMIN")
    void getAllIgnoredTagDto() throws Exception {
        this.mvc.perform(get("/api/user/tag/ignored"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {
            "datasets/tagDatasets/tagTrack.yml"
    })
    @WithMockUser (username = "123@mail.com", authorities = "ADMIN")
    void getAllTrackedTagDto() throws Exception {
        this.mvc.perform(get("/api/user/tag/tracked"))
                .andExpect(status().isOk());
    }
}
