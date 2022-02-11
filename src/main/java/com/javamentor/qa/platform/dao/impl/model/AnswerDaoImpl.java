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
    public AnswerDaoImpl (EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Answer getAnswerByQuestionIdAndUserIdAndAnswerBody (Long questionId, Long userId, String htmlBody) {
        return entityManager.createQuery("FROM Answer a WHERE a.question.id =:questionId and a.user.id =: userId and a.htmlBody =:htmlBody", Answer.class)
                .setParameter("questionId", questionId)
                .setParameter("userId", userId)
                .setParameter("htmlBody", htmlBody)
                .getSingleResult();
    }
}
