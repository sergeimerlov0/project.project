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
        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto." +
                        "QuestionDto(question.id, question.title, author.id, " +
                        "(SELECT sum (reputation.count) from Reputation reputation where reputation.author.id = :id), " +
                        "author.fullName, author.imageLink, " +
                        "question.description, 0L, " +
                        "(SELECT count (*) from Answer answer where answer.question.id = :id), " +
                        "((SELECT count (*) from VoteQuestion voteOnQuestion " +
                        "where voteOnQuestion.vote = 'UP_VOTE' and voteOnQuestion.question.id = :id) - " +
                        "(SELECT count (*) from VoteQuestion voteOnQuestion " +
                        "where voteOnQuestion.vote = 'DOWN_VOTE' and voteOnQuestion.question.id = :id)), " +
                        "question.persistDateTime, question.lastUpdateDateTime) " +
                        "from Question question " +
                        "join question.user as author " +
                        "left outer join question.answers as answer " +
                        "where question.id is null", QuestionDto.class)
                .setFirstResult((currentPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return (int) entityManager.createQuery("SELECT count (id) FROM Question").getSingleResult();
    }
}
