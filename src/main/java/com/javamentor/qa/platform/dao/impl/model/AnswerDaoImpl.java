package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {

    private final EntityManager entityManager;

    @Autowired
    public AnswerDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Answer> getListAnswerByQuestionIdAndUserId(Long questionId, Long userId) {
        return entityManager.createQuery("SELECT a FROM Answer a  join fetch a.user  " +
                        "WHERE a.question.id =:questionId and a.user.id =: userId ", Answer.class)
                .setParameter("questionId", questionId)
                .setParameter("userId", userId)
                .getResultList();
    }

}
