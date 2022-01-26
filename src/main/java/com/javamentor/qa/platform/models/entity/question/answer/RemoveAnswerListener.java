package com.javamentor.qa.platform.models.entity.question.answer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PreRemove;

public class RemoveAnswerListener {

    @PersistenceContext()
    private EntityManager entityManager;

    @PreRemove
    public void preRemove(Answer answer) {
        entityManager.createQuery("delete from CommentAnswer where answer.id=" + answer.getId())
                .executeUpdate();
        entityManager.createQuery("delete from VoteAnswer where answer.id=" + answer.getId())
                .executeUpdate();
    }
}