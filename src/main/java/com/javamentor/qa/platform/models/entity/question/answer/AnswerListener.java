package com.javamentor.qa.platform.models.entity.question.answer;

import org.hibernate.annotations.Where;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PreRemove;

public class AnswerListener {

    @PersistenceContext()
    private EntityManager entityManager;

    @PreRemove
    public void preRemove(Answer answer) {
        Long answerId = answer.getId();
        entityManager.createQuery("delete from CommentAnswer where answer.id=:answerId")
                .setParameter("answerId", answerId)
                .executeUpdate();
        entityManager.createQuery("delete from VoteAnswer where answer.id=:answerId")
                .setParameter("answerId", answerId)
                .executeUpdate();
    }
}