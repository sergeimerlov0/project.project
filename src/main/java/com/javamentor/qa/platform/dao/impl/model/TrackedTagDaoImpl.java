package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TrackedTagDaoImpl extends ReadWriteDaoImpl<TrackedTag, Long> implements TrackedTagDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Long> trackedTagsByUserId(Long userId) {
        return entityManager.createQuery(
                "SELECT t.trackedTag.id " +
                        "FROM TrackedTag t " +
                        "WHERE t.user.id = :id",
                        Long.class)
                .setParameter("id", userId)
                .getResultList();
    }

    @Override
    public boolean tagIsPresentInTheListOfUser(Long userId, Long tagId) {
        long count = entityManager.createQuery(
                "SELECT COUNT(t) " +
                        "FROM TrackedTag t " +
                        "WHERE t.user.id = :userId " +
                        "AND t.trackedTag.id = :tagId",
                        Long.class)
                .setParameter("userId", userId)
                .setParameter("tagId", tagId)
                .getSingleResult();
        return count > 0;
    }
}