package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation, Long> implements ReputationDao {
    @PersistenceContext()
    private EntityManager entityManager;

    @Override
    public Reputation getReputationByAnswerId(Long answerId) {
        return entityManager.createQuery(
                "SELECT rep " +
                        "FROM Reputation rep " +
                        "WHERE rep.answer.id = :id",
                        Reputation.class)
                .setParameter("id", answerId)
                .getSingleResult();
    }
}