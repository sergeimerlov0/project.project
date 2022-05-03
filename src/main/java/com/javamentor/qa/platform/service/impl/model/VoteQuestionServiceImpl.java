package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteQuestionDao;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class VoteQuestionServiceImpl extends ReadWriteServiceImpl<VoteQuestion, Long> implements VoteQuestionService {
    private VoteQuestionDao voteQuestionDao;
    private ReputationDao reputationDao;

    @Autowired
    public VoteQuestionServiceImpl(VoteQuestionDao voteQuestionDao, ReputationDao reputationDao) {
        super(voteQuestionDao);
        this.voteQuestionDao = voteQuestionDao;
        this.reputationDao = reputationDao;
    }

    @Override
    public Integer getTotalVoteQuestionsByQuestionId(Long questionId) {
        return voteQuestionDao.getTotalVoteQuestionsByQuestionId(questionId);
    }

    @Override
    public boolean userVoteCheck(Long questionId, Long userId) {
        return voteQuestionDao.userVoteCheck(questionId, userId);
    }

    @Override
    public void persist(VoteQuestion voteQuestion) {
        Reputation reputation = new Reputation(LocalDateTime.now(), voteQuestion.getQuestion().getUser(), voteQuestion.getUser(), voteQuestion.getVote().getValue(), ReputationType.VoteQuestion, voteQuestion.getQuestion());
        reputationDao.persist(reputation);
        super.persist(voteQuestion);
    }

    @Override
    public Optional<VoteQuestion> getByUserIdAndQuestionId(Long userId, Long questionId) {
        return voteQuestionDao.getByUserIdAndQuestionId(userId, questionId);
    }
}