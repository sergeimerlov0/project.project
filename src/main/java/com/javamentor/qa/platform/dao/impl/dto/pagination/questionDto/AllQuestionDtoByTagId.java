package com.javamentor.qa.platform.dao.impl.dto.pagination.questionDto;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("AllQuestionDtoByTagId")
@RequiredArgsConstructor
public class AllQuestionDtoByTagId implements PaginationDtoAble<QuestionDto> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<QuestionDto> getItems(Map<String, Object> param) {
        Long id = (Long) param.get("tagId");
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        return entityManager.createQuery(
                "SELECT DISTINCT new com.javamentor.qa.platform.models.dto." +
                        "QuestionDto(question.id, question.title, author.id, " +
                        "(SELECT sum (reputation.count) from Reputation reputation where reputation.author.id = author.id), " +
                        "author.fullName, author.imageLink, " +
                        "question.description, 0L, " +
                        "(SELECT count (*) from Answer answer where answer.question.id = question.id), " +
                        "((SELECT count (*) from VoteQuestion voteOnQuestion " +
                        "where voteOnQuestion.vote = 'UP_VOTE' and voteOnQuestion.question.id = question.id) - " +
                        "(SELECT count (*) from VoteQuestion voteOnQuestion " +
                        "where voteOnQuestion.vote = 'DOWN_VOTE' and voteOnQuestion.question.id = question.id)), " +
                        "question.persistDateTime, question.lastUpdateDateTime) " +
                        "from Question question " +
                        "left outer join question.user as author on (question.user.id=author.id) " +
                        "left outer join question.answers as answer on (question.id=answer.question.id) " +
                        "left outer join question.tags as tag " +
                        "where tag.id = :id and question.isDeleted=false order by question.id", QuestionDto.class)
                .setParameter("id", id)
                .getResultStream()
                .skip((currentPageNumber-1)*itemsOnPage).limit(itemsOnPage).collect(Collectors.toList());
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        Long id = (Long) param.get("tagId");
        return Math.toIntExact((Long) entityManager.createQuery(
                        "SELECT count(*) from Question question left outer join question.tags as tag where tag.id=:id and question.isDeleted=false")
                .setParameter("id", id).getSingleResult());
    }
}
