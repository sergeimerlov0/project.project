package com.javamentor.qa.platform.dao.impl.dto.pagination.questionDto;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("AllQuestionsDtoByVoteAndAnswerAndViewByMonth")
@RequiredArgsConstructor
public class AllQuestionsDtoByVoteAndAnswerAndViewByMonth implements PaginationDtoAble<QuestionViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<Long> trackedTags = (List<Long>) param.get("trackedTags");
        List<Long> ignoredTags = (List<Long>) param.get("ignoredTags");

        return entityManager.createQuery(
                        "SELECT DISTINCT new com.javamentor.qa.platform.models.dto.QuestionViewDto" +
                                "(question.id, question.title, author.id, " +
                                "(SELECT SUM (reputation.count) " +
                                "FROM Reputation reputation " +
                                "WHERE reputation.author.id = author.id), " +
                                "author.fullName, author.imageLink, question.description, 0L, " +
                                "(SELECT COUNT (*) " +
                                "FROM Answer answer " +
                                "WHERE answer.question.id = question.id) as countAnswer, " +
                                "((SELECT COUNT (*) " +
                                "FROM VoteQuestion voteOnQuestion " +
                                "WHERE voteOnQuestion.vote = 'UP_VOTE' AND voteOnQuestion.question.id = question.id) - " +
                                "(SELECT COUNT (*) " +
                                "FROM VoteQuestion voteOnQuestion " +
                                "WHERE voteOnQuestion.vote = 'DOWN_VOTE' AND voteOnQuestion.question.id = question.id)) as countVote, " +
                                "question.persistDateTime, question.lastUpdateDateTime," +
                                "(SELECT DISTINCT  CASE WHEN EXISTS (SELECT  b.id FROM BookMarks b WHERE b.user.id = author.id AND b.question.id = question.id) THEN true ELSE false END as isUserBookmark FROM BookMarks ) )" +
                                "FROM Question question " +
                                "LEFT JOIN question.user AS author ON (question.user.id = author.id) " +
                                "LEFT JOIN question.answers AS answer ON (question.id = answer.question.id) " +
                                "WHERE question.persistDateTime >= current_date()-30 AND question.isDeleted = false " +
                                "AND question.id IN (SELECT question FROM Question question JOIN question.tags AS tag WHERE :trackedTags IS NULL OR tag.id IN :trackedTags) " +
                                "AND question.id NOT IN (SELECT question FROM Question question JOIN question.tags AS tag WHERE tag.id = :ignoredTags)" +
                                "ORDER BY countVote, countAnswer"
                        , QuestionViewDto.class)
                .setParameter("trackedTags", trackedTags)
                .setParameter("ignoredTags", ignoredTags)
                .getResultStream()
                .skip((long) itemsOnPage * (currentPageNumber - 1))
                .limit(itemsOnPage)
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        List<Long> trackedTags = (List<Long>) param.get("trackedTags");
        List<Long> ignoredTags = (List<Long>) param.get("ignoredTags");

        return Math.toIntExact((long) entityManager.createQuery(
                        "SELECT COUNT(*) FROM Question question " +
                                "WHERE question.persistDateTime >= current_date()-30 AND question.isDeleted = false " +
                                "AND question.id IN (SELECT question FROM Question question JOIN question.tags AS tag WHERE :trackedTags IS NULL OR tag.id IN :trackedTags) " +
                                "AND question.id NOT IN (SELECT question FROM Question question JOIN question.tags AS tag WHERE tag.id = :ignoredTags)")
                .setParameter("trackedTags", trackedTags)
                .setParameter("ignoredTags", ignoredTags)
                .getSingleResult());
    }
}