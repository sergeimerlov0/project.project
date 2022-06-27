package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteQuestionDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class VoteQuestionDaoImpl extends ReadWriteDaoImpl<VoteQuestion, Long> implements VoteQuestionDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Integer getTotalVoteQuestionsByQuestionId(Long questionId) {

        return ((Long) entityManager.createQuery(
                "SELECT (SELECT COUNT(*) FROM VoteQuestion v " +
                        " WHERE v.vote = 'UP_VOTE' AND v.question.id = :questionId ) " +
                        " - (SELECT COUNT(*) FROM VoteQuestion v " +
                        " WHERE v.vote = 'DOWN_VOTE' AND v.question.id = :questionId ) " +
                        " FROM VoteQuestion v WHERE v.question.id = :questionId "
                )
                .setParameter("questionId", questionId)
                .getSingleResult()).intValue();
    }

    @Override
    public boolean userVoteCheck(Long questionId, Long userId) {
        List<VoteQuestion> voteQuestionList = entityManager.createQuery(
                "FROM VoteQuestion a " +
                        "WHERE a.question.id = :questionId " +
                        "AND a.user.id = :userId",
                        VoteQuestion.class)
                .setParameter("questionId", questionId)
                .setParameter("userId", userId)
                .getResultList();
        return voteQuestionList.isEmpty();
    }

    @Override
    public Optional<VoteQuestion> getByUserIdAndQuestionId(Long userId, Long questionId) {
        TypedQuery<VoteQuestion> result = entityManager.createQuery(
                "FROM VoteQuestion a " +
                        "INNER JOIN FETCH a.question q " +
                        "INNER JOIN FETCH q.user " +
                        "WHERE a.question.id = :questionId " +
                        "AND a.user.id = :userId", VoteQuestion.class)
                .setParameter("questionId", questionId)
                .setParameter("userId", userId);
        return SingleResultUtil.getSingleResultOrNull(result);
    }
}