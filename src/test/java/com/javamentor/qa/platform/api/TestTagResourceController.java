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
            "datasets/TagResourceController/getRelatedTagDto/tag.yml",
            "datasets/TagResourceController/getRelatedTagDto/question.yml",
            "datasets/TagResourceController/getRelatedTagDto/user.yml",
            "datasets/TagResourceController/getRelatedTagDto/role.yml",
            "datasets/TagResourceController/getRelatedTagDto/questionHasTag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getRelatedTagDto() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/tag/related")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[4].id", is(104)))
                .andExpect(jsonPath("$[8].id", is(108)))
                .andExpect(jsonPath("$[0].countQuestion", is(10)))
                .andExpect(jsonPath("$[1].countQuestion", is(9)))
                .andExpect(jsonPath("$[2].countQuestion", is(8)))
                .andExpect(jsonPath("$[3].countQuestion", is(7)))
                .andExpect(jsonPath("$[4].countQuestion", is(6)))
                .andExpect(jsonPath("$[5].countQuestion", is(5)))
                .andExpect(jsonPath("$[6].countQuestion", is(4)))
                .andExpect(jsonPath("$[7].countQuestion", is(3)))
                .andExpect(jsonPath("$[8].countQuestion", is(2)))
                .andExpect(jsonPath("$[9].countQuestion", is(1)));
    }

    @Test
    @DataSet(value = {
            "datasets/TagResourceController/getTrackedAndIgnoredTagDto/user.yml",
            "datasets/TagResourceController/getTrackedAndIgnoredTagDto/role.yml",
            "datasets/TagResourceController/getTrackedAndIgnoredTagDto/tag.yml",
            "datasets/TagResourceController/getTrackedAndIgnoredTagDto/tagIgnored.yml",
            "datasets/TagResourceController/getTrackedAndIgnoredTagDto/tagTracked.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getTrackedAndIgnoredTagDto() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/user/tag/ignored")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());
        Assertions.assertTrue(em.createQuery(
                "SELECT t.ignoredTag.id " +
                        "FROM IgnoredTag t " +
                        "WHERE t.user.id = :id",
                        Long.class)
                .setParameter("id", 100L)
                .getResultList()
                .containsAll(Arrays.asList(100L, 101L)));

        mvc.perform(MockMvcRequestBuilders.get("/api/user/tag/tracked")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());
        Assertions.assertTrue(em.createQuery(
                "SELECT t.trackedTag.id " +
                        "FROM TrackedTag t " +
                        "WHERE t.user.id = :id",
                        Long.class)
                .setParameter("id", 100L)
                .getResultList()
                .containsAll(Arrays.asList(102L, 103L)));
    }

    @Test
    @DataSet(value = {
            "datasets/TagResourceController/testAddTrackedAndIgnoredTag/role.yml",
            "datasets/TagResourceController/testAddTrackedAndIgnoredTag/user.yml",
            "datasets/TagResourceController/testAddTrackedAndIgnoredTag/tag.yml",
            "datasets/TagResourceController/testAddTrackedAndIgnoredTag/tagIgnored.yml",
            "datasets/TagResourceController/testAddTrackedAndIgnoredTag/tagTracked.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void testAddTrackedAndIgnoredTag() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/user/tag/102/ignored")
                        .header("Authorization", getJwtToken("123@mail.com", "password")))
                .andExpect(status().isOk());
        Assertions.assertTrue(em.createQuery(
                "SELECT t.ignoredTag.id " +
                        "FROM IgnoredTag t " +
                        "WHERE t.user.id = :id",
                        Long.class)
                .setParameter("id", 101L)
                .getResultList()
                .containsAll(Arrays.asList(100L, 101L, 102L)));

        mvc.perform(MockMvcRequestBuilders.post("/api/user/tag/105/tracked")
                        .header("Authorization", getJwtToken("123@mail.com", "password")))
                .andExpect(status().isOk());
        Assertions.assertTrue(em.createQuery(
                "SELECT t.trackedTag.id " +
                        "FROM TrackedTag t " +
                        "WHERE t.user.id = :id",
                        Long.class)
                .setParameter("id", 101L)
                .getResultList()
                .containsAll(Arrays.asList(103L, 104L, 105L)));
    }

    @Test
    @DataSet(value = {
            "datasets/TagResourceController/getTagsWithString/tag.yml",
            "datasets/TagResourceController/getTagsWithString/question.yml",
            "datasets/TagResourceController/getTagsWithString/user.yml",
            "datasets/TagResourceController/getTagsWithString/role.yml",
            "datasets/TagResourceController/getTagsWithString/questionHasTag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getTagsWithString() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/user/tag/latter?string=tag10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].name", is("tag100")))
                .andExpect(jsonPath("$[1].name", is("tag101")))
                .andExpect(jsonPath("$[2].name", is("tag102")))
                .andExpect(jsonPath("$[3].name", is("tag103")));

    }

    @Test
    @DataSet(value = {
            "datasets/TagResourceController/getTagsWithString_ShouldReturn10Tags/tag.yml",
            "datasets/TagResourceController/getTagsWithString_ShouldReturn10Tags/question.yml",
            "datasets/TagResourceController/getTagsWithString_ShouldReturn10Tags/user.yml",
            "datasets/TagResourceController/getTagsWithString_ShouldReturn10Tags/role.yml",
            "datasets/TagResourceController/getTagsWithString_ShouldReturn10Tags/questionHasTag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getTagsWithString_ShouldReturn10Tags() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/user/tag/latter?string=tag")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                //tag100: 10 questions (100 - 109)
                .andExpect(jsonPath("$[0].name", is("tag100")))
                //tag101: 9 questions (100 - 108)
                .andExpect(jsonPath("$[1].name", is("tag101")))
                //tag102: 8 questions (100 - 109)
                .andExpect(jsonPath("$[2].name", is("tag102")))
                //tag103: 7 questions (100 - 109)
                .andExpect(jsonPath("$[3].name", is("tag103")))
                //tag110: 6 questions (100 - 109)
                .andExpect(jsonPath("$[4].name", is("tag110")));

    }

    @Test
    @DataSet(value = {
            "datasets/TagResourceController/getTagsSorted/tag.yml",
            "datasets/TagResourceController/getTagsSorted/question.yml",
            "datasets/TagResourceController/getTagsSorted/user.yml",
            "datasets/TagResourceController/getTagsSorted/role.yml",
            "datasets/TagResourceController/getTagsSorted/questionHasTag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getTagsSorted() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/user/tag/name?currentPageNumber=1")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.totalResultCount", is(16)))
                .andExpect(jsonPath("$.items.length()", is(10)))
                .andExpect(jsonPath("$.items[0].title", is("tag100")))
                .andExpect(jsonPath("$.items[4].title", is("tag104")))
                .andExpect(jsonPath("$.items[7].title", is("tag107")))
                .andExpect(jsonPath("$.items[9].title", is("tag109")));
    }

    @Test
    @DataSet(value = {
            "datasets/TagResourceController/getTagsSorted_Items5Page1_ShouldReturn5Tags/tag.yml",
            "datasets/TagResourceController/getTagsSorted_Items5Page1_ShouldReturn5Tags/question.yml",
            "datasets/TagResourceController/getTagsSorted_Items5Page1_ShouldReturn5Tags/user.yml",
            "datasets/TagResourceController/getTagsSorted_Items5Page1_ShouldReturn5Tags/role.yml",
            "datasets/TagResourceController/getTagsSorted_Items5Page1_ShouldReturn5Tags/questionHasTag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getTagsSorted_Items5Page1_ShouldReturn5Tags() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/user/tag/name?currentPageNumber=1&itemsOnPage=5")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.totalResultCount", is(16)))
                .andExpect(jsonPath("$.items.length()", is(5)))
                .andExpect(jsonPath("$.items[0].title", is("tag100")))
                .andExpect(jsonPath("$.items[1].title", is("tag101")))
                .andExpect(jsonPath("$.items[2].title", is("tag102")))
                .andExpect(jsonPath("$.items[3].title", is("tag103")))
                .andExpect(jsonPath("$.items[4].title", is("tag104")));
    }

    @Test
    @DataSet(value = {
            "datasets/TagResourceController/getTagsSorted/tag.yml",
            "datasets/TagResourceController/getTagsSorted/question.yml",
            "datasets/TagResourceController/getTagsSorted/user.yml",
            "datasets/TagResourceController/getTagsSorted/role.yml",
            "datasets/TagResourceController/getTagsSorted/questionHasTag.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getTagsSortedWithFilter() throws Exception {
        //список тегов с фильтром "JavA" без учета регистра
        mvc.perform(MockMvcRequestBuilders.get("/api/user/tag/name?currentPageNumber=1")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111"))
                        .param("filter", "JavA"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.totalResultCount", is(16)))
                .andExpect(jsonPath("$.items.length()", is(2)))
                .andExpect(jsonPath("$.items[0].title", is("JavaScript")))
                .andExpect(jsonPath("$.items[1].title", is("Java")));
    }

    @Test
    @DataSet(value = {
            "datasets/TagResourceController/getTagsWithString/tag.yml",
            "datasets/TagResourceController/getTagsWithString/question.yml",
            "datasets/TagResourceController/getTagsWithString/user.yml",
            "datasets/TagResourceController/getTagsWithString/role.yml",
            "datasets/TagResourceController/getTagsWithString/questionHasTag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getTagsWithStringWithFilter() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/user/tag/latter?string=tag10")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111"))
                        .param("filter", "jaVaScript")) //вот этот фильтр нигде не учитывается
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].name", is("tag100")))
                .andExpect(jsonPath("$[1].name", is("tag101")))
                .andExpect(jsonPath("$[2].name", is("tag102")))
                .andExpect(jsonPath("$[3].name", is("tag103")));

    }
}