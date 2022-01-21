package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.models.dto.CommentDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentDtoDaoImpl implements CommentDtoDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<CommentDto> getCommentDtosByQuestionId(Long id) {
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.CommentDto" +
                        "(comment.id, " +
                        "comment.text, " +
                        "comment.user.id, " +
                        "comment.user.fullName, " +
                        "(SELECT SUM (r.count) FROM Reputation r WHERE r.author.id = comment.user.id), " +
                        "comment.persistDateTime)" +
                        "FROM Comment comment " +
                        "JOIN CommentQuestion commentQuestion ON (comment.id = commentQuestion.comment.id) " +
                        "WHERE commentQuestion.question.id = :id"
                , CommentDto.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Map<Long, List<CommentDto>> getMapCommentDtosByQuestionIds(List<Long> ids) {
        List<Tuple> comments = entityManager.createQuery("SELECT c.id as comment_id, " +
                        "c.text as comment_text, " +
                        "c.user.id as user_id, " +
                        "c.user.fullName as user_fullName, " +
                        "(SELECT SUM (r.count) FROM Reputation r WHERE r.author.id = c.user.id) as user_reputation, " +
                        "c.persistDateTime as comment_dateAdded, " +
                        "cq.question.id as question_id " +
                        "FROM Comment c " +
                        "JOIN CommentQuestion cq ON (c.id = cq.comment.id) " +
                        "WHERE cq.question.id in :ids", Tuple.class)
                .setParameter("ids", ids)
                .getResultList();

        Map<Long, List<CommentDto>> commentsMap = new HashMap<>();

        comments.forEach(tuple -> commentsMap.computeIfAbsent(
                tuple.get("question_id", Long.class), v -> new ArrayList<>())
                .add(new CommentDto(
                        tuple.get("comment_id", Long.class),
                        tuple.get("comment_text", String.class),
                        tuple.get("user_id", Long.class),
                        tuple.get("user_fullName", String.class),
                        tuple.get("user_reputation", Long.class),
                        tuple.get("comment_dateAdded", LocalDateTime.class))));

        return commentsMap;
    }
}
