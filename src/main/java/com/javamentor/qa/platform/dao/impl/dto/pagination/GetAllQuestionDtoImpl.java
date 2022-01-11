package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("AllQuestionDto")
@RequiredArgsConstructor
public class GetAllQuestionDtoImpl implements PaginationDtoAble<QuestionDto> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<QuestionDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
//        ArrayList<Long> trackedTag = (ArrayList<Long>) param.get("trackedTags");
//        ArrayList<Long> ignoredTag = (ArrayList<Long>) param.get("ignoredTags");

        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.QuestionDto" +
                        "(question.id, question.title, author.id, " +
                        "(SELECT SUM (reputation.count) " +
                        "FROM Reputation reputation " +
                        "WHERE reputation.author.id = author.id), " +
                        "author.fullName, author.imageLink, question.description, 0L, " +
                        "(SELECT COUNT (*) " +
                        "FROM Answer answer " +
                        "WHERE answer.question.id = question.id), " +
                        "((SELECT COUNT (*) " +
                        "FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'UP_VOTE' AND voteOnQuestion.question.id = question.id) - " +
                        "(SELECT COUNT (*) " +
                        "FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'DOWN_VOTE' AND voteOnQuestion.question.id = question.id)), " +
                        "question.persistDateTime, question.lastUpdateDateTime)" +
                        "FROM Question question " +
                        "LEFT JOIN question.user AS author ON (question.user.id = author.id) " +
                        "LEFT JOIN question.answers AS answer ON (question.id = answer.question.id)"
                        , QuestionDto.class)
                .getResultStream()
                .skip(itemsOnPage * (currentPageNumber - 1))
                .limit(itemsOnPage)
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return Math.toIntExact((long) entityManager.createQuery(
                "SELECT count (*) " +
                        "FROM Question question")
                .getSingleResult());
    }
}
