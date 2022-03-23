package com.javamentor.qa.platform.dao.impl.dto.pagination.questionDto;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("AllQuestionsByVoteAndAnswerByWeek")
@RequiredArgsConstructor
public class AllQuestionsByVoteAndAnswerAndViewByWeek implements PaginationDtoAble<QuestionViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<Long> trackedTags = ((List<Long>) param.get("tracked"));
        List<Long> ignoredTags = ((List<Long>) param.get("ignored"));
        return entityManager.createQuery(
                "SELECT DISTINCT NEW com.javamentor.qa.platform.models.dto.QuestionViewDto" +
                        "(q.id, q.title, q.user.id, " +
                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE q.user.id = reputation.author.id), " +
                        "q.user.fullName, q.user.imageLink, q.description, 0L," +
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
                        "AND question.isDeleted = false " +
                        "ORDER BY countAnswer + countVoite ASC"
                        , QuestionViewDto.class)
                .setParameter("ignored", ignoredTags)
                .setParameter("tracked", trackedTags)
                .setParameter("week", LocalDateTime.now().minusDays(7L))
                .getResultStream()
                .skip((long) itemsOnPage * (currentPageNumber - 1))
                .limit(itemsOnPage)
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return Math.toIntExact((long) entityManager.createQuery(
                        "SELECT count (*) FROM Question question WHERE question.isDeleted = false")
                .getSingleResult());
    }
}
