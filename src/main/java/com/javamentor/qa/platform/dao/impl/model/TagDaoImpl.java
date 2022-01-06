package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class TagDaoImpl extends ReadWriteDaoImpl<Tag, Long> implements TagDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tag> getByName(String name) {
        TypedQuery<Tag> query = (TypedQuery<Tag>) entityManager.createQuery("FROM Tag WHERE name = :name").setParameter("name", name);
        return SingleResultUtil.getSingleResultOrNull(query);
    }
}
