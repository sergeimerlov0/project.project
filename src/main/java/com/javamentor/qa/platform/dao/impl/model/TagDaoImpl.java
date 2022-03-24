package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TagDaoImpl extends ReadWriteDaoImpl<Tag, Long> implements TagDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> findTagsByNames(List<String> tagNames) {
        return entityManager.createQuery("FROM Tag t WHERE t.name IN :names", Tag.class)
                .setParameter("names", tagNames)
                .getResultList();
    }
}