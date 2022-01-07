package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class VoteAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer, Long> implements VoteAnswerDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Long getTotalVotesByAnswerId(Long id) {
        return (Long) entityManager.createQuery("select ((select count(*) from VoteAnswer v where v.answer.id =:id and v.vote = 'UP_VOTE') - " +
                        "(select count(*) from VoteAnswer v where v.answer.id =:id and v.vote = 'DOWN_VOTE')) from VoteAnswer v where v.answer.id = :id")
                .setParameter("id",id).getSingleResult();
    }
}

