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
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.QuestionDto" +
                        "(question.id, question.title, question.description, question.persistDateTime, " +
                        "question.lastUpdateDateTime, question.user.id, question.user.fullName, " +
                        "question.user.imageLink, sum(reputation.count), count(answer.question.id), " +
                        "((select count(up.vote) from VoteQuestion up " +
                        "where up.vote = 'UP_VOTE' and up.user.id = question.user.id) - " +
                        "(select count(down.vote) from VoteQuestion down " +
                        "where down.vote = 'DOWN_VOTE' and down.user.id = question.user.id)))" +
                        "from Question question left join Reputation reputation " +
                        "on question.user.id = reputation.author.id " +
                        "left join Answer answer on question.id = answer.question.id where " +
                        "group by question.id , question.user.fullName, question.user.imageLink " +
                        "order by sum(reputation.count)", QuestionDto.class)
                .setFirstResult((currentPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return (int) entityManager.createQuery("SELECT count (id) FROM Question").getSingleResult();
    }
}
