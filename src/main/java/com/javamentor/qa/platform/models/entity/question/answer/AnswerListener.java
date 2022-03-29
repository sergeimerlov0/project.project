package com.javamentor.qa.platform.models.entity.question.answer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PreRemove;

public class AnswerListener {
    @PersistenceContext()
    private EntityManager entityManager;

    @PreRemove
    public void preRemove(Answer answer) {
        Long answerId = answer.getId();
        entityManager.createQuery("DELETE FROM Reputation WHERE answer.id = :answerId")
                .setParameter("answerId", answerId)
                .executeUpdate();
    }
}