package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.IgnoredTagDao;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class IgnoredTagDaoImpl extends ReadWriteDaoImpl<IgnoredTag, Long> implements IgnoredTagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Long> ignoredTagsByUserId(Long userId) {
        return entityManager
                .createQuery("select t.ignoredTag.id from IgnoredTag t where t.user.id=:id", Long.class)
                .setParameter("id", userId).getResultList();
    }

    @Override
    public boolean tagIsPresentInTheListOfUser(Long userId, Long tagId) {
        long count = entityManager
                .createQuery("select count(t) from IgnoredTag t where t.user.id=:userId and t.ignoredTag.id=:tagId", Long.class)
                .setParameter("userId", userId)
                .setParameter("tagId", tagId)
                .getSingleResult();
        return count > 0;
    }
}
