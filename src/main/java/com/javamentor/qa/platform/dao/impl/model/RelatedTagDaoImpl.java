package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.RelatedTagDao;
import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.RelatedTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RelatedTagDaoImpl extends ReadWriteDaoImpl<RelatedTag, Long> implements RelatedTagDao {
    @PersistenceContext
    private EntityManager entityManager;
}