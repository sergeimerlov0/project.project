package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> getTrackedTagByUsername(String username) {
        return entityManager.createQuery("SELECT t FROM TrackedTag t WHERE t.user.email=:username", Tag.class)
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public List<Tag> getIgnoreTagByUsername(String username) {
        return entityManager.createQuery("SELECT t FROM IgnoredTag t WHERE t.user.email=:username", Tag.class)
                .setParameter("username", username)
                .getResultList();
    }
}
