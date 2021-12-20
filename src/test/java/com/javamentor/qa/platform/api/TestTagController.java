package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestTagController extends AbstractApiTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserService userService;

    @Test
    @DataSet(value = {
            "datasets/tagDatasets/role.yml",
            "datasets/tagDatasets/user.yml",
            "datasets/tagDatasets/tag.yml",
            "datasets/tagDatasets/tagIgnore.yml"
    })
    void getAllIgnoredTagDto() throws Exception {
        this.mvc.perform(get("/api/user/tag/ignored"))
                .andExpect(status().isOk());
        Assertions.assertTrue(idTest(1));
    }

    @Test
    @DataSet(value = {
            "datasets/tagDatasets/tagTrack.yml"
    })
    void getAllTrackedTagDto() throws Exception {
        this.mvc.perform(get("/api/user/tag/tracked"))
                .andExpect(status().isOk());
        Assertions.assertTrue(idTest(0));
    }

    boolean idTest(int x) {
        if (x == 1) {
            for (Long id : entityManager.createQuery("select t.ignoredTag.id from IgnoredTag t where t.user.id=:id", Long.class)
                    .setParameter("id", 2L)
                    .getResultList()) {
                if (!(id == 100 || id == 101)) {
                    return false;
                }
            }
        } else {
            for (Long id : entityManager.createQuery("select t.trackedTag.id from TrackedTag t where t.user.id=:id", Long.class)
                    .setParameter("id", 2L)
                    .getResultList()) {
                if (!(id == 102 || id == 103)) {
                    return false;
                }
            }
        }
        return true;
    }
}
