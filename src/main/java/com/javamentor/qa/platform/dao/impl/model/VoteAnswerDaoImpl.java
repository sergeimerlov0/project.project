package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class VoteAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer, Long> implements VoteAnswerDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Long getTotalVotesByAnswerId(Long id) {
        return (Long) entityManager.createQuery(
                "SELECT " +
                        "((SELECT COUNT(*) FROM VoteAnswer v WHERE v.answer.id = :id AND v.vote = 'UP_VOTE') - " +
                        "(SELECT COUNT(*) FROM VoteAnswer v WHERE v.answer.id = :id AND v.vote = 'DOWN_VOTE')) " +
                        "FROM VoteAnswer v " +
                        "WHERE v.answer.id = :id")
                .setParameter("id",id)
                .getSingleResult();
    }

    @Override
    public Optional<VoteAnswer> getByUserIdAndAnswerId(Long userId, Long answerId) {
        TypedQuery<VoteAnswer> result = entityManager.createQuery(
                        "FROM VoteAnswer a " +
                                "INNER JOIN FETCH a.answer q " +
                                "INNER JOIN FETCH q.user " +
                                "WHERE a.answer.id = :answerId " +
                                "AND a.user.id = :userId", VoteAnswer.class)
                .setParameter("answerId", answerId)
                .setParameter("userId", userId);
        return SingleResultUtil.getSingleResultOrNull(result);
    }
}