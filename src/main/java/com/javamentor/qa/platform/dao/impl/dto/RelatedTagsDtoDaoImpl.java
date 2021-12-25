package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.RelatedTagsDtoDao;
import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


/**
 * \* Created with IntelliJ IDEA.
 * \* User: Rustam
 */

@Repository
public class RelatedTagsDtoDaoImpl implements RelatedTagsDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RelatedTagsDto> getRelatedTagsDto() {
        return entityManager.createQuery("SELECT  new  com.javamentor.qa.platform.models.dto.RelatedTagsDto" +
                "(t.id, t.name, t.questions.size) " +
                "from Tag t inner join t.questions group by t.id order by t.questions.size desc ", RelatedTagsDto.class)
                .setFirstResult(0)
                .setMaxResults(10)
                .getResultList();
    }
}

