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
    public Answer getAnswerByQuestionIdAndUserIdAndAnswerBody(Long questionId, Long userId, String htmlBody) {
        List<Answer> answerList = entityManager.createQuery("SELECT a FROM Answer a  join fetch a.user  " +
                        "WHERE a.question.id =:questionId and a.user.id =: userId " +
                        "and a.htmlBody =:htmlBody", Answer.class)
                .setParameter("questionId", questionId)
                .setParameter("userId", userId)
                .setParameter("htmlBody", htmlBody)
                .getResultList();
        return answerList.get(answerList.size() - 1);
    }

    @Override
    public Long getUpVoteCountByAnswer(Answer answer) {
        return entityManager.createQuery("select count (*) from VoteAnswer v where v.vote = 'UP_VOTE' " +
                        "and v.answer.id = :id", Long.class)
                .setParameter("id", answer.getId())
                .getSingleResult();
    }

    @Override
    public Long getDownVoteCountByAnswer(Answer answer) {
        return entityManager.createQuery("select count (*) from VoteAnswer v where v.vote = 'DOWN_VOTE' " +
                        "and v.answer.id = :id", Long.class)
                .setParameter("id", answer.getId())
                .getSingleResult();
    }
}
