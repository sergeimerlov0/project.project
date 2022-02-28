package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
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
                        "select a.id, a.user.id, " +
                                "sum(r.count), " +
                                "u.imageLink, u.nickname, " +
                                "a.question.id, " +
                                "a.htmlBody, a.persistDateTime, a.isHelpful, a.dateAcceptTime ," +
                                "((select count(*) from VoteAnswer v where v.vote = 'UP_VOTE' and v.answer.id = a.id) - (select count(*) from VoteAnswer v where v.vote = 'DOWN_VOTE' and v.answer.id = a.id)) " +
                                "from Answer a left outer join Reputation r on (a.user.id = r.author.id) " +
                                "left outer join User u on (a.user.id = u.id)" +
                                "where a.question.id =: id and a.isDeleted = false " +
                                "group by a.id, a.user.id, u.imageLink, u.nickname")
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
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.AnswerDto(" +
                        "a.id, u.id,sum(r.count),u.imageLink, u.nickname ,a.question.id, a.htmlBody, a.persistDateTime, " +
                        "a.isHelpful, a.dateAcceptTime," +
                        "((select count(*) from VoteAnswer v where v.vote = 'UP_VOTE' and v.answer.id = a.id)" +
                        " + (select count(*) from VoteAnswer v where v.vote = 'DOWN_VOTE' and v.answer.id = a.id )))" +
                        " from Answer a inner join User u on (a.user.id = u.id) " +
                        "left outer join Reputation r on (a.user.id = r.author.id) " +
                        "where a.id =:id group by a.id, u.id, u.imageLink, u.nickname", AnswerDto.class)
                .setParameter("id", answerId)
                .getResultStream().findAny();
    }
}







