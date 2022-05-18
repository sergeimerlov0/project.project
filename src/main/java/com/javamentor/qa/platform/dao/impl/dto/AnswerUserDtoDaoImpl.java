package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerUserDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerUserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Rustam
 */
@Repository
public class AnswerUserDtoDaoImpl implements AnswerUserDtoDao {
    @PersistenceContext()
    private EntityManager entityManager;

    @Override
    public List<AnswerUserDto> getAnswerByQuestionIdForLastWeek() {

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

}