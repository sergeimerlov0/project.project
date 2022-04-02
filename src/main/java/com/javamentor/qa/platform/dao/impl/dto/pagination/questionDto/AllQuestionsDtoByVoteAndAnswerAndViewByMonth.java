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
    EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");

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

                                "question.persistDateTime, question.lastUpdateDateTime)" +
                                "FROM Question question " +
                                "LEFT JOIN question.user AS author ON (question.user.id = author.id) " +
                                "LEFT JOIN question.answers AS answer ON (question.id = answer.question.id) " +
                                "WHERE question.isDeleted = false AND question.persistDateTime >= current_date()-30" +
                                "ORDER BY countVote, countAnswer"
                        , QuestionViewDto.class)
                .getResultStream()
                .skip((long) itemsOnPage * (currentPageNumber - 1))
                .limit(itemsOnPage)
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return Math.toIntExact((long) entityManager.createQuery(
                        "SELECT count (*) FROM Question question WHERE question.isDeleted = false AND question.persistDateTime >= current_date()-30")
                .getSingleResult());
    }
}

//                        "SELECT DISTINCT new com.javamentor.qa.platform.models.dto.QuestionViewDto" +
//                                "(question.id, question.title, author.id, " +
//
//                                "(SELECT SUM (reputation.count) " +
//                                "FROM Reputation reputation " +
//                                "WHERE reputation.author.id = author.id), " +
//
//                                "author.fullName, author.imageLink, question.description, 0L, " +
//
//                                "(SELECT COUNT (*) " +
//                                "FROM Answer answer " +
//                                "WHERE answer.question.id = question.id) as countAnswer, " +
//
//                                "((SELECT COUNT (*) " +
//                                "FROM VoteQuestion voteOnQuestion " +
//                                "WHERE voteOnQuestion.vote = 'UP_VOTE' AND voteOnQuestion.question.id = question.id) - " +
//
//                                "(SELECT COUNT (*) " +
//                                "FROM VoteQuestion voteOnQuestion " +
//                                "WHERE voteOnQuestion.vote = 'DOWN_VOTE' AND voteOnQuestion.question.id = question.id)) as countVote, " +
//
//                                "question.persistDateTime, question.lastUpdateDateTime)" +
//                                "FROM Question question " +
//                                "LEFT JOIN question.user AS author ON (question.user.id = author.id) " +
//                                "LEFT JOIN question.answers AS answer ON (question.id = answer.question.id) " +
//                                "WHERE question.isDeleted = false AND question.persistDateTime >= current_date()-30" +
//                                "ORDER BY countVote, countAnswer"
