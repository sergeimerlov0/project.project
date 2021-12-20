package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getTrackedTagById(Long id) {
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.TagDto(t.trackedTag) from TrackedTag t where t.user.id=:id", TagDto.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<TagDto> getIgnoreTagById(Long id) {
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.TagDto(t.ignoredTag) from IgnoredTag t where t.user.id=:id", TagDto.class)
                .setParameter("id", id)
                .getResultList();
    }
}
