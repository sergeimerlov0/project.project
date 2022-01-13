package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("QuestionDtoNoAnswer")
public class GetQuestionDtoNoAnswer implements PaginationDtoAble<QuestionDto> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<QuestionDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<Long> trackedTags = ((List<Long>) param.get("trackedTags"));
        List<Long> ignoredTags = ((List<Long>) param.get("ignoredTags"));

        //@Todo в дальнейшем реализовать подсчёт просмотров (пока стоит SUM(0))

        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.QuestionDto" +
                        "(q.id, q.title, q.user.id, " +
                        "(SELECT SUM (r.count) FROM Reputation r WHERE q.user.id = r.author.id), " +
                        "q.user.fullName, q.user.imageLink, q.description, " +
                        "SUM(0), " +
                        "(SELECT COUNT(a.id) FROM Answer a WHERE a.question.id = q.id), " +
                        "((SELECT COUNT (*) FROM VoteQuestion v " +
                        "WHERE v.vote = 'UP_VOTE' AND v.question.id = question.id) - " +
                        "(SELECT COUNT (*) FROM VoteQuestion v " +
                        "WHERE v.vote = 'DOWN_VOTE' AND v.question.id = question.id)), " +
                        "q.persistDateTime, q.lastUpdateDateTime) " +
                        "FROM Question q  " +
                        "JOIN q.tags tgs " +
                        "WHERE q.id IN (SELECT q.id From Question q JOIN q.tags tgs " +
                        "WHERE :tracked IS NULL OR tgs.id IN :tracked) " +
                        "AND q.id NOT IN (SELECT q.id From Question q JOIN q.tags tgs " +
                        "WHERE tgs.id IN :ignored) " +
                        "AND q.id NOT IN (SELECT a.question.id FROM Answer a) AND q.isDeleted = false " +
                        "GROUP BY q.id , q.user.fullName, q.user.imageLink ORDER BY q.id ", QuestionDto.class)
                .setParameter("tracked", trackedTags)
                .setParameter("ignored", ignoredTags)
                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        List<Long> ignoredTags = (List<Long>) param.get("ignoredTags");
        List<Long> trackedTags = (List<Long>) param.get("trackedTags");
        return Math.toIntExact((Long) entityManager.createQuery("SELECT COUNT(DISTINCT q.id) " +
                        "FROM Question q JOIN q.tags tgs " +
                        "WHERE q.id NOT IN (SELECT a.question.id FROM Answer a ) " +
                        "AND q.id IN (SELECT q.id From Question q JOIN q.tags tgs " +
                        "WHERE :tracked IS NULL OR tgs.id IN :tracked) " +
                        "AND q.id NOT IN (SELECT q.id From Question q JOIN q.tags tgs " +
                        "WHERE tgs.id IN :ignored) " +
                        "AND q.isDeleted = false")
                .setParameter("tracked", trackedTags)
                .setParameter("ignored", ignoredTags)
                .getSingleResult());
    }
}
