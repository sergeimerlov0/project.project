package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestTagResourceController extends AbstractApiTest {

    @Test
    @DataSet(value = {
            "datasets/TagResourceController/tagRelatedDatasets/tag.yml",
            "datasets/TagResourceController/tagRelatedDatasets/answer.yml",
            "datasets/TagResourceController/tagRelatedDatasets/question.yml",
            "datasets/TagResourceController/tagRelatedDatasets/user.yml",
            "datasets/TagResourceController/tagRelatedDatasets/role.yml",
            "datasets/TagResourceController/tagRelatedDatasets/questionHasTag.yml"
    })
    public void getRelatedTagDto() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/tag/related")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
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
    @DataSet(value = {
            "datasets/TagResourceController/tagTrackedAndIgnoredDatasets/user.yml",
            "datasets/TagResourceController/tagTrackedAndIgnoredDatasets/role.yml",
            "datasets/TagResourceController/tagTrackedAndIgnoredDatasets/tag.yml",
            "datasets/TagResourceController/tagTrackedAndIgnoredDatasets/tagIgnored.yml",
            "datasets/TagResourceController/tagTrackedAndIgnoredDatasets/tagTracked.yml"
    })
    public void getTrackedAndIgnoredTagDto() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/user/tag/ignored")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());
        Assertions.assertTrue(em.createQuery("select t.ignoredTag.id from IgnoredTag t " +
                        "where t.user.id=:id", Long.class)
                .setParameter("id", 100L)
                .getResultList()
                .containsAll(Arrays.asList(100L, 101L)));

        mvc.perform(MockMvcRequestBuilders.get("/api/user/tag/tracked")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());
        Assertions.assertTrue(em.createQuery("select t.trackedTag.id from TrackedTag t " +
                        "where t.user.id=:id", Long.class)
                .setParameter("id", 100L)
                .getResultList()
                .containsAll(Arrays.asList(102L, 103L)));
    }

    @Test
    @DataSet(value = {
            "datasets/TagResourceController/addTrackedAndIgnoredTagDatasets/role.yml",
            "datasets/TagResourceController/addTrackedAndIgnoredTagDatasets/user.yml",
            "datasets/TagResourceController/addTrackedAndIgnoredTagDatasets/tag.yml",
            "datasets/TagResourceController/addTrackedAndIgnoredTagDatasets/tagIgnored.yml",
            "datasets/TagResourceController/addTrackedAndIgnoredTagDatasets/tagTracked.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void testAddTrackedAndIgnoredTag() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/user/tag/102/ignored")
                        .header("Authorization", getJwtToken("123@mail.com", "password")))
                .andExpect(status().isOk());
        Assertions.assertTrue(em.createQuery("select t.ignoredTag.id from IgnoredTag t " +
                        "where t.user.id=:id", Long.class)
                .setParameter("id", 101L)
                .getResultList()
                .containsAll(Arrays.asList(100L, 101L, 102L)));

        mvc.perform(MockMvcRequestBuilders.post("/api/user/tag/105/tracked")
                        .header("Authorization", getJwtToken("123@mail.com", "password")))
                .andExpect(status().isOk());
        Assertions.assertTrue(em.createQuery("select t.trackedTag.id from TrackedTag t " +
                        "where t.user.id=:id", Long.class)
                .setParameter("id", 101L)
                .getResultList()
                .containsAll(Arrays.asList(103L, 104L, 105L)));
    }
}
