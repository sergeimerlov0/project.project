package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {

    private final EntityManager entityManager;

    @Autowired
    public AnswerDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Boolean checkAnswerByQuestionIdAndUserId(Long questionId, Long userId) {
        return entityManager.createQuery("SELECT COUNT(a)>0 FROM Answer a  left join User u on a.id = u.id " +
                        "WHERE a.question.id =:questionId and a.user.id =: userId ", Boolean.class)
                .setParameter("questionId", questionId)
                .setParameter("userId", userId)
                .getSingleResult();
    }

}
