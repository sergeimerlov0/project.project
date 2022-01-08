package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionCommentDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionCommentDtoDaoImpl implements QuestionCommentDtoDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<QuestionCommentDto> getQuestionCommentByQuestionId(Long id) {
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.QuestionCommentDto (c.id, " +
                        "cq.question.id," +
                        "c.lastUpdateDateTime," +
                        "c.persistDateTime," +
                        "c.text," +
                        "c.user.id," +
                        "u.imageLink," +
                        "(SELECT sum (r.count) from Reputation r where r.author.id = u.id))" +
                        "FROM Comment c " +
                        "INNER JOIN CommentQuestion cq ON(c.id = cq.comment.id)" +
                        "LEFT OUTER JOIN User u ON(c.user.id = u.id)" +
                        "WHERE cq.question.id = :id"
                , QuestionCommentDto.class)
                .setParameter("id", id)
                .getResultList();
    }
}
