package com.javamentor.qa.platform.dao.impl.dto.pagination.questionDto;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository("AllQuestionDtoSortedByDate")
public class AllQuestionDtoSortedByDate implements PaginationDtoAble<QuestionViewDto> {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<QuestionViewDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<Long> trackedTags = ((List<Long>) param.get("tracked"));
        List<Long> ignoredTags = ((List<Long>) param.get("ignored"));
//        return entityManager.createQuery(
//                "SELECT DISTINCT NEW com.javamentor.qa.platform.models.dto.QuestionViewDto" +
//                        "(q.id, " +
//                        "q.title, " +
//                        "q.user.id, " +
//                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE q.user.id = reputation.author.id), " +
//                        "q.user.fullName, " +
//                        "q.user.imageLink, " +
//                        "q.description, " +
//                        "0L, " +
//                        "(SELECT COUNT(a.id) FROM Answer a WHERE a.question.id = q.id), " +
//                        "((SELECT COUNT (*) FROM VoteQuestion v WHERE v.vote = 'UP_VOTE' AND v.question.id = question.id) - " +
//                        "(SELECT COUNT (*) FROM VoteQuestion v WHERE v.vote = 'DOWN_VOTE' AND v.question.id = question.id)), " +
//                        "q.persistDateTime, " +
//                        "q.lastUpdateDateTime) " +
//                        "FROM Question q  " +
//                        "JOIN q.tags tgs " +
//                        "WHERE q.id IN (SELECT q.id From Question q JOIN q.tags tgs WHERE :tracked IS NULL OR tgs.id IN :tracked) " +
//                        "AND q.id NOT IN (SELECT q.id From Question q JOIN q.tags tgs WHERE tgs.id IN :ignored) " +
//                        "ORDER BY q.persistDateTime ASC",
//                        QuestionViewDto.class)
//                .setParameter("tracked", trackedTags)
//                .setParameter("ignored", ignoredTags)
//                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
//                .setMaxResults(itemsOnPage)
//                .getResultList();

        return entityManager.createQuery(
                        "SELECT DISTINCT NEW com.javamentor.qa.platform.models.dto.QuestionViewDto" +
                                "(q.id, " +
                                "q.title, " +
                                "q.user.id, " +
                                "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE q.user.id = reputation.author.id), " +
                                "q.user.fullName, " +
                                "q.user.imageLink, " +
                                "q.description, " +
                                "0L, " +
                                "(SELECT COUNT(*) FROM Answer a WHERE a.question.id = q.id) AS countAnswer, " +
                                "((SELECT COUNT(*) FROM VoteQuestion v WHERE v.vote = 'UP_VOTE' AND v.question.id = question.id) - " +
                                "(SELECT COUNT(*) FROM VoteQuestion v WHERE v.vote = 'DOWN_VOTE' AND v.question.id = question.id)) AS countVoite, " +
                                "q.persistDateTime, " +
                                "q.lastUpdateDateTime) " +
                                "FROM Question q " +
                                "JOIN q.tags tgs " +
                                "WHERE q.id IN (SELECT q.id From Question q JOIN q.tags tgs WHERE :tracked IS NULL OR tgs.id IN :tracked) " +
                                "AND q.id NOT IN (SELECT q.id From Question q JOIN q.tags tgs WHERE tgs.id IN :ignored) " +
                                "AND q.persistDateTime BETWEEN :week AND current_date " +
                                "ORDER BY countAnswer + countVoite ASC",
                        QuestionViewDto.class)
                .setParameter("tracked", trackedTags)
                .setParameter("ignored", ignoredTags)
                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getTotalResultCount(Map<String, Object> param) {
        List<Long> ignoredTags = (List<Long>) param.get("ignored");
        List<Long> trackedTags = (List<Long>) param.get("tracked");
        return Math.toIntExact((Long) entityManager.createQuery(
                "SELECT COUNT(DISTINCT q.id) " +
                        "FROM Question q " +
                        "JOIN q.tags tgs " +
                        "WHERE q.id IN (SELECT q.id From Question q JOIN q.tags tgs WHERE :tracked IS NULL OR tgs.id IN :tracked) " +
                        "AND q.id NOT IN (SELECT q.id From Question q JOIN q.tags tgs WHERE tgs.id IN :ignored)")
                .setParameter("tracked", trackedTags)
                .setParameter("ignored", ignoredTags)
                .getSingleResult());
    }
}