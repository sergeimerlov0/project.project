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

        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.QuestionDto" +
                                "(question.id, " +
                                "question.title, " +
                                "user.id, " +
                                "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = user.id)," +
                                "user.fullName, " +
                                "user.imageLink, " +
                                "question.description, " +
                                "0L, " +
                                "(SELECT COUNT(*) FROM Answer answer WHERE answer.question.id = :id AND answer.isDeleted = false), " +
                                "((SELECT COUNT(*) FROM VoteQuestion voteOnQuestion WHERE voteOnQuestion.vote = 'UP_VOTE' AND voteOnQuestion.question.id = :id) + " +
                                "(SELECT COUNT(*) FROM VoteQuestion voteOnQuestion WHERE voteOnQuestion.vote = 'DOWN_VOTE' AND voteOnQuestion.question.id = :id)), " +
                                "question.persistDateTime, " +
                                "question.lastUpdateDateTime," +
                                "(SELECT v.vote FROM VoteQuestion v WHERE v.user.id = user.id AND v.question.id = :id)) " +
                                "FROM Question question " +
                                "LEFT JOIN User user ON user.id = question.user.id " +
                                "LEFT JOIN Answer answer ON answer.question.id = :id " +
                                "WHERE question.id = :id " +
                                "AND question.isDeleted = false " +
                                "ORDER BY question.id ASC",
                        QuestionDto.class)
                .setParameter("id", id)
                .getResultStream()
                .findAny();
    }

    @Override
    public List<QuestionCommentDto> getQuestionCommentByQuestionId(Long id) {
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.QuestionCommentDto " +
                                "(c.id, " +
                                "cq.question.id, " +
                                "c.lastUpdateDateTime, " +
                                "c.persistDateTime, " +
                                "c.text, " +
                                "c.user.id, " +
                                "u.imageLink, " +
                                "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = c.user.id))" +
                                "FROM Comment c " +
                                "INNER JOIN CommentQuestion cq ON c.id = cq.comment.id " +
                                "LEFT OUTER JOIN User u ON c.user.id = u.id " +
                                "WHERE cq.question.id = :id",
                        QuestionCommentDto.class)
                .setParameter("id", id)
                .getResultList();
    }
}