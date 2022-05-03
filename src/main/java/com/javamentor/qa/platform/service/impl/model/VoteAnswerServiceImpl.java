package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteAnswerServiceImpl extends ReadWriteServiceImpl<VoteAnswer, Long> implements VoteAnswerService {
    private final VoteAnswerDao voteAnswerDao;
    private final ReputationDao reputationDao;
    private final static Integer UP_VOTE = 10;
    private final static Integer DOWN_VOTE = -5;

    @Autowired
    public VoteAnswerServiceImpl(VoteAnswerDao voteAnswerDao, ReputationDao reputationDao) {
        super(voteAnswerDao);
        this.voteAnswerDao = voteAnswerDao;
        this.reputationDao = reputationDao;
    }

    @Override
    public Long getTotalVotesByAnswerId(Long id) {
        return voteAnswerDao.getTotalVotesByAnswerId(id);
    }

    @Override
    @Transactional
    public Long postVoteUp(User userSender, Answer answer) {
        voteAnswerDao.persist(new VoteAnswer(userSender, answer, VoteType.UP_VOTE));
        reputationDao.persist(new Reputation(answer.getUser(), userSender, UP_VOTE, ReputationType.VoteAnswer, answer));
        return voteAnswerDao.getTotalVotesByAnswerId(answer.getId());
    }

    @Override
    @Transactional
    public Long postVoteDown(User userSender, Answer answer) {
        voteAnswerDao.persist(new VoteAnswer(userSender, answer, VoteType.DOWN_VOTE));
        reputationDao.persist(new Reputation(answer.getUser(), userSender, DOWN_VOTE, ReputationType.VoteAnswer, answer));
        return voteAnswerDao.getTotalVotesByAnswerId(answer.getId());
    }

    @Override
    @Transactional
    public Optional<VoteAnswer> getByUserIdAndAnswerId(Long userId, Long answerId) {
            return voteAnswerDao.getByUserIdAndAnswerId(userId, answerId);
    }
}