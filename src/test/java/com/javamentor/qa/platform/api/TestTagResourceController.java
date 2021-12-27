package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Rustam
 */

@DataSet(cleanBefore = true)
public class TestTagResourceController extends AbstractApiTest {



    @Test
    @DataSet(value = {
            "relatedTagsDto/tag.yml",
            "relatedTagsDto/answer.yml",
            "relatedTagsDto/question.yml",
            "relatedTagsDto/user.yml",
            "relatedTagsDto/role.yml",
            "relatedTagsDto/questionHasTag.yml"
    })
    public void getRelatedTagDto() throws Exception {

        this.mvc.perform(get("/api/user/tag/related"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].countQuestion", is(10)))
                .andExpect(jsonPath("$[4].id", is(104)))
                .andExpect(jsonPath("$[4].countQuestion", is(6)))
                .andExpect(jsonPath("$[8].id", is(108)))
                .andExpect(jsonPath("$[8].countQuestion", is(2)));
    }

    @Test
    public void getEmptyListRelatedTagDto() throws Exception {

        this.mvc.perform(get("/api/user/tag/related"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].countQuestion", is(10)))
                .andExpect(jsonPath("$[4].id", is(104)))
                .andExpect(jsonPath("$[4].countQuestion", is(6)))
                .andExpect(jsonPath("$[8].id", is(108)))
                .andExpect(jsonPath("$[8].countQuestion", is(2)));
    }


}
