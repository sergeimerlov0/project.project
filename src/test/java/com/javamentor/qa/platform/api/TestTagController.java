package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestTagController extends AbstractApiTest {

    @Test
    @DataSet(value = {
            "datasets/tagDatasets/role.yml",
            "datasets/tagDatasets/user.yml",
            "datasets/tagDatasets/tag.yml",
            "datasets/tagDatasets/tagIgonre.yml",
    })
    void getAllIgnoredTagDto() throws Exception {
        this.mvc.perform(get("/api/user/tag/ignore"))
                .andExpect(status().isOk());
    }
}
