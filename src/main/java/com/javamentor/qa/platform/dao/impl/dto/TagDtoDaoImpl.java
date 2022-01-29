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
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto." +
                "TagDto(tag.id, tag.name, tag.description) " +
                "from Tag tag " +
                "join tag.questions as questions " +
                "where questions.id=: id", TagDto.class).setParameter("id", id).getResultList();
    }

    @Override
    public List<TagDto> getTagsTop10WithString(String partTag) {
        partTag = '%' + partTag + '%';
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto." +
                "TagDto(tag.id, tag.name, tag.description) from Tag tag where tag.name LIKE :partTag " +
                        "ORDER BY tag.questions.size desc", TagDto.class)
                .setParameter("partTag", partTag)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public Map<Long, List<TagDto>> getMapTagsByQuestionIds(List<Long> questionIds){
        List<Tuple> tags = entityManager.createQuery("SELECT t.id as tag_id, t.name as tag_name, " +
                        "t.description as tag_description, " +
                        "q.id as question_id From Tag t JOIN t.questions q WHERE q.id in :ids", Tuple.class)
                .setParameter("ids", questionIds)
                .getResultList();
        Map<Long, List<TagDto>> tagsMap = new HashMap<>();
        tags.forEach(tuple -> tagsMap.computeIfAbsent(tuple.get("question_id", Long.class),
                        key -> new ArrayList<>())
                .add(new TagDto(tuple.get("tag_id", Long.class), tuple.get("tag_name", String.class),
                        tuple.get("tag_description", String.class))));
        return tagsMap;
    }

    @Override
    public List<TagDto> getTrackedTagById(Long id) {
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto." +
                        "TagDto(x.id, x.name) from TrackedTag t join t.trackedTag x where t.user.id=:id", TagDto.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<TagDto> getIgnoreTagById(Long id) {
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.TagDto" +
                        "(x.id, x.name) from IgnoredTag t join t.ignoredTag x where t.user.id=:id", TagDto.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<RelatedTagsDto> getRelatedTagsDto() {
        return entityManager.createQuery("SELECT new  com.javamentor.qa.platform.models.dto.RelatedTagsDto" +
                        "(t.id, t.name, t.questions.size) " +
                        "from Tag t inner join t.questions " +
                        "group by t.id order by t.questions.size desc ", RelatedTagsDto.class)
                .setMaxResults(10)
                .getResultList();
    }
}
