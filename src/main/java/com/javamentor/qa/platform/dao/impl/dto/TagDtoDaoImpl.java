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
    EntityManager entityManager;

    @Override
    public List<TagDto> getTagsByQuestionId(Long id) {
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto." +
                "TagDto(tag.id, tag.name, tag.description) " +
                "from Tag tag " +
                "join tag.questions as questions " +
                "where questions.id=: id", TagDto.class).setParameter("id", id).getResultList();
    }

    @Override
    public List<TagDto> getTrackedTagById(Long id) {
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.TagDto(x.id, x.name) from TrackedTag t join t.trackedTag x where t.user.id=:id", TagDto.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<TagDto> getIgnoreTagById(Long id) {
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.TagDto(x.id, x.name) from IgnoredTag t join t.ignoredTag x where t.user.id=:id", TagDto.class)
                .setParameter("id", id)
                .getResultList();
    }
}
