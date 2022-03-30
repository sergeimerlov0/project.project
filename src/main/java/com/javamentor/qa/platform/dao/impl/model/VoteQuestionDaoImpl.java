package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteQuestionDao;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class VoteQuestionDaoImpl extends ReadWriteDaoImpl<VoteQuestion, Long> implements VoteQuestionDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Integer getTotalVoteQuestionsByQuestionId(Long questionId) {
        List<VoteQuestion> voteQuestionList = entityManager.createQuery(
                "FROM VoteQuestion a " +
                        "WHERE a.question.id = :questionId",
                        VoteQuestion.class)
                .setParameter("questionId", questionId)
                .getResultList();
        return voteQuestionList.size();
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
}