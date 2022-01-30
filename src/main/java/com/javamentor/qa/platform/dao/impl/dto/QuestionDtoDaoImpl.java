package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuestionDtoDaoImpl implements QuestionDtoDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<QuestionDto> getQuestionDtoByQuestionId(Long id) {

        //@Todo в дальнейшем реализовать подсчёт просмотров (пока стоит 0)

        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto." +
                "QuestionDto(question.id, question.title, author.id, " +
                "(SELECT sum (reputation.count) from Reputation reputation where reputation.author.id = :id), " +
                "author.fullname, author.imageLink, " +
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
                "where question.id = :id", QuestionDto.class).setParameter("id", id).getResultStream().findAny();
    }

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
