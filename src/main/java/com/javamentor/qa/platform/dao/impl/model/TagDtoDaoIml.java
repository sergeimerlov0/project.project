package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TagDtoDaoIml extends ReadOnlyDaoImpl<TagDto, Long> implements TagDtoDao {
    @PersistenceContext
    private EntityManager entityManager;
}
