package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.dto.AnswerUserDto;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Rustam
 */
@Repository
public class AnswerDtoDaoImpl implements AnswerDtoDao {
    @PersistenceContext()
    private EntityManager entityManager;

    @Override
    public List<AnswerDto> getAnswerByQuestionId(Long id) {
        return (List<AnswerDto>) entityManager.createQuery(
                        "SELECT " +
                                "a.id, " +
                                "a.user.id, " +
                                "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = a.user.id), " +
                                "u.imageLink, " +
                                "u.nickname, " +
                                "a.question.id, " +
                                "a.htmlBody, " +
                                "a.persistDateTime, " +
                                "a.isHelpful, " +
                                "a.dateAcceptTime, " +
                                "((SELECT COUNT(*) FROM VoteAnswer v WHERE v.vote = 'UP_VOTE' AND v.answer.id = a.id) - " +
                                "(SELECT COUNT(*) FROM VoteAnswer v WHERE v.vote = 'DOWN_VOTE' AND v.answer.id = a.id)) " +
                                "FROM Answer a " +
                                "LEFT OUTER JOIN User u ON a.user.id = u.id " +
                                "WHERE a.question.id = :id " +
                                "AND a.isDeleted = false " +
                                "GROUP BY a.id, a.user.id, u.imageLink, u.nickname")
                .setParameter("id", id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(
                        new ResultTransformer() {
                            @Override
                            public Object transformTuple(Object[] tuple, String[] aliases) {
                                AnswerDto answerDto = new AnswerDto();
                                answerDto.setId((Long) tuple[0]);
                                answerDto.setUserId((Long) tuple[1]);
                                answerDto.setUserReputation((Long) tuple[2]);
                                answerDto.setImage((String) tuple[3]);
                                answerDto.setNickName((String) tuple[4]);
                                answerDto.setQuestionId((Long) tuple[5]);
                                answerDto.setBody((String) tuple[6]);
                                answerDto.setPersistDate((LocalDateTime) tuple[7]);
                                answerDto.setIsHelpful((Boolean) tuple[8]);
                                answerDto.setPersistDate((LocalDateTime) tuple[9]);
                                answerDto.setCountValuable((Long) tuple[10]);

                                return answerDto;
                            }

                            @Override
                            public List transformList(List tuples) {
                                return tuples;
                            }
                        }
                ).getResultList();
    }

    @Override
    public Optional<AnswerDto> getAnswerDtoById(Long answerId) {
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.AnswerDto " +
                        "(a.id, " +
                        "u.id, " +
                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = a.user.id), " +
                        "u.imageLink, " +
                        "u.nickname, " +
                        "a.question.id, " +
                        "a.htmlBody, " +
                        "a.persistDateTime, " +
                        "a.isHelpful, " +
                        "a.dateAcceptTime, " +
                        "((SELECT COUNT(*) FROM VoteAnswer v WHERE v.vote = 'UP_VOTE' AND v.answer.id = a.id) + " +
                        "(SELECT COUNT(*) FROM VoteAnswer v WHERE v.vote = 'DOWN_VOTE' AND v.answer.id = a.id))) " +
                        "FROM Answer a " +
                        "INNER JOIN User u ON u.id = a.user.id " +
                        "WHERE a.id = :id " +
                        "GROUP BY a.id, u.id, u.imageLink, u.nickname",
                        AnswerDto.class)
                .setParameter("id", answerId)
                .getResultStream()
                .findAny();
    }

    @Override
    public List<AnswerUserDto> getAnswerForLastWeek() {
        List<Tuple> ansLastWeek = entityManager.createQuery(
                        "SELECT a.id AS answer_id, " +
                                "a.question.id AS question_id, " +
                                "a.persistDateTime AS persist_date, " +
                                "a.htmlBody AS html_body, " +
                                "(SELECT COUNT(*) FROM VoteAnswer v WHERE v.answer.id = a.id AND v.vote = 'UP_VOTE') " +
                                " - (SELECT COUNT(*) FROM VoteAnswer v WHERE v.answer.id = a.id AND v.vote = 'DOWN_VOTE') " +
                                "AS count_answer_vote " +
                                "FROM Answer a LEFT JOIN VoteAnswer v ON v.answer.id = a.id WHERE a.persistDateTime >= current_date() - 7 " +
                                "GROUP BY a.id", Tuple.class)
                .getResultList();

        List<AnswerUserDto> ansLastWeekList = new ArrayList<>();
        ansLastWeek.forEach(tuple -> ansLastWeekList.add(new AnswerUserDto(
                tuple.get("answer_id", Long.class),
                tuple.get("question_id", Long.class),
                tuple.get("count_answer_vote", Long.class),
                tuple.get("persist_date", LocalDateTime.class),
                tuple.get("html_body", String.class))));

        return ansLastWeekList;
    }

    @Override
    public List<AnswerDto> getDeletedAnswersByUserId(Long userId) {
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.AnswerDto " +
                        "(a.id, " +
                        "u.id, " +
                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = a.user.id), " +
                        "u.imageLink, " +
                        "u.nickname, " +
                        "a.user.id, " +
                        "a.htmlBody, " +
                        "a.persistDateTime, " +
                        "a.isHelpful, " +
                        "a.dateAcceptTime, " +
                        "((SELECT COUNT(*) FROM VoteAnswer v WHERE v.vote = 'UP_VOTE' AND v.answer.id = a.id) + " +
                        "(SELECT COUNT(*) FROM VoteAnswer v WHERE v.vote = 'DOWN_VOTE' AND v.answer.id = a.id))) " +
                        "FROM Answer a " +
                        "INNER JOIN User u ON u.id = a.user.id " +
                        "WHERE u.id = :id " +
                        "AND (a.isDeleted = true OR a.isDeletedByModerator = true) " +
                        "GROUP BY a.id, u.id, u.imageLink, u.nickname",
                        AnswerDto.class)
                .setParameter("id", userId)
                .getResultList();
    }
}