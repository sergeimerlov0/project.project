package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<TagDto> getTagsByQuestionId(Long id) {
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.TagDto " +
                        "(tag.id, tag.name, tag.description) " +
                        "FROM Tag tag " +
                        "JOIN tag.questions AS questions " +
                        "WHERE questions.id = :id",
                        TagDto.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<TagDto> getTagsTop10WithString(String partTag) {
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.TagDto (tag.id, tag.name, tag.description) " +
                        "FROM Tag tag " +
                        "WHERE LOWER(tag.name) LIKE LOWER(CONCAT('%', :partTag, '%'))  " +
                        "ORDER BY tag.questions.size DESC",
                        TagDto.class)
                .setParameter("partTag", partTag)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public Map<Long, List<TagDto>> getMapTagsByQuestionIds(List<Long> questionIds){
        List<Tuple> tags = entityManager.createQuery(
                "SELECT t.id AS tag_id, " +
                        "t.name AS tag_name, " +
                        "t.description AS tag_description, " +
                        "q.id AS question_id " +
                        "From Tag t " +
                        "JOIN t.questions q " +
                        "WHERE q.id IN :ids",
                        Tuple.class)
                .setParameter("ids", questionIds)
                .getResultList();

        Map<Long, List<TagDto>> tagsMap = new HashMap<>();

        tags.forEach(tuple -> tagsMap.computeIfAbsent(tuple.get("question_id", Long.class), key -> new ArrayList<>())
                .add(new TagDto(tuple.get("tag_id", Long.class),
                        tuple.get("tag_name", String.class),
                        tuple.get("tag_description", String.class))));
        return tagsMap;
    }

    @Override
    public List<TagDto> getTrackedTagById(Long id) {
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.TagDto " +
                        "(x.id, x.name) " +
                        "FROM TrackedTag t " +
                        "JOIN t.trackedTag x " +
                        "WHERE t.user.id = :id",
                        TagDto.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<TagDto> getIgnoreTagById(Long id) {
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.TagDto " +
                        "(x.id, x.name) " +
                        "FROM IgnoredTag t " +
                        "JOIN t.ignoredTag x " +
                        "WHERE t.user.id = :id",
                        TagDto.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<RelatedTagsDto> getRelatedTagsDto() {
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.RelatedTagsDto " +
                        "(t.id, t.name, t.questions.size) " +
                        "FROM Tag t " +
                        "INNER JOIN t.questions " +
                        "GROUP BY t.id " +
                        "ORDER BY t.questions.size DESC",
                        RelatedTagsDto.class)
                .setMaxResults(10)
                .getResultList();
    }
}