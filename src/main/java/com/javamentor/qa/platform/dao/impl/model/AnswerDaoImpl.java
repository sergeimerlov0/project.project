package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean checkAnswerByQuestionIdAndUserId(Long questionId, Long userId) {
        return entityManager.createQuery(
                "SELECT COUNT(a)>0 " +
                        "FROM Answer a " +
                        "LEFT JOIN User u ON a.id = u.id " +
                        "WHERE a.question.id = :questionId " +
                        "AND a.user.id = :userId",
                        Boolean.class)
                .setParameter("questionId", questionId)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}