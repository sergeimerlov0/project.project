package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getTrackedTagById(Long id) {
        return convertInDto(entityManager.createQuery("SELECT t FROM TrackedTag t WHERE t.user.id=:id", Tag.class)
                .setParameter("id", id)
                .getResultList());
    }

    @Override
    public List<TagDto> getIgnoreTagById(Long id) {
        return convertInDto(entityManager.createQuery("SELECT t FROM IgnoredTag t WHERE t.user.id=:id", Tag.class)
                .setParameter("id", id)
                .getResultList());
    }

    private List<TagDto> convertInDto(List<Tag> tags) {
        List<TagDto> tagDtos = new ArrayList<>();
        for (Tag tag : tags) {
            tagDtos.add(new TagDto(tag.getId(), tag.getName()));
        }
        return tagDtos;
    }
}
