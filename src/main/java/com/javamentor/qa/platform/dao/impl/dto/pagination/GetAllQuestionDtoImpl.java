package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("AllQuestionDto")
@RequiredArgsConstructor
public class GetAllQuestionDtoImpl implements PaginationDtoAble<QuestionViewDto> {
    @PersistenceContext
   private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<Long> trackedTags = (List<Long>) param.get("trackedTags");
        List<Long> ignoredTags = (List<Long>) param.get("ignoredTags");
        return entityManager.createQuery(
                "SELECT DISTINCT new com.javamentor.qa.platform.models.dto.QuestionViewDto " +
                        "(question.id, " +
                        "question.title, " +
                        "author.id, " +
                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = author.id), " +
                        "author.fullName, " +
                        "author.imageLink, " +
                        "question.description, " +
                        "0L, " +
                        "(SELECT COUNT (*) FROM Answer answer WHERE answer.question.id = question.id), " +
                        "((SELECT COUNT (*) FROM VoteQuestion voteOnQuestion WHERE voteOnQuestion.vote = 'UP_VOTE' AND voteOnQuestion.question.id = question.id) - " +
                        "(SELECT COUNT (*) FROM VoteQuestion voteOnQuestion WHERE voteOnQuestion.vote = 'DOWN_VOTE' AND voteOnQuestion.question.id = question.id)), " +
                        "question.persistDateTime, " +
                        "question.lastUpdateDateTime) " +
                        "FROM Question question " +
                        "JOIN question.tags AS tags " +
                        "LEFT JOIN question.user AS author ON author.id = question.user.id " +
                        "LEFT JOIN question.answers AS answer ON answer.question.id = question.id " +
                        "WHERE question.id IN (SELECT q.id FROM Question q JOIN q.tags AS tags WHERE :trackedTags IS NULL OR tags.id IN :trackedTags) " +
                        "AND question.id NOT IN (SELECT q.id FROM Question q JOIN q.tags AS tags WHERE tags.id IN :ignoredTags) " +
                        "AND question.isDeleted = false",
                        QuestionViewDto.class)
                .setParameter("trackedTags", trackedTags)
                .setParameter("ignoredTags", ignoredTags)
                .getResultStream()
                .skip(itemsOnPage * (currentPageNumber - 1))
                .limit(itemsOnPage)
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        List<Long> trackedTags = (List<Long>) param.get("trackedTags");
        List<Long> ignoredTags = (List<Long>) param.get("ignoredTags");
        return Math.toIntExact((long) entityManager.createQuery(
                "SELECT COUNT(DISTINCT question.id) " +
                        "FROM Question question " +
                        "JOIN question.tags AS tags " +
                        "WHERE question.id IN (SELECT q.id FROM Question q JOIN q.tags AS tags WHERE :trackedTags IS NULL OR tags.id IN :trackedTags) " +
                        "AND question.id NOT IN (SELECT q.id FROM Question q JOIN q.tags AS tags WHERE tags.id IN :ignoredTags) " +
                        "AND question.isDeleted = false")
                .setParameter("trackedTags", trackedTags)
                .setParameter("ignoredTags", ignoredTags)
                .getSingleResult());
    }
}