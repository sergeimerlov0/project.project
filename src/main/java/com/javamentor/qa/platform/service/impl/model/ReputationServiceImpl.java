package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReputationServiceImpl extends ReadWriteServiceImpl <Reputation, Long> implements ReputationService {

    ReputationDao reputationDao;
    @Autowired
    public ReputationServiceImpl (ReputationDao reputationDao){
        super(reputationDao);
        this.reputationDao = reputationDao;
    }

    @Override
    public Reputation setReputationCount(VoteQuestion voteQuestion, Long questionId){
        Reputation reputation = reputationDao.getById(questionId).get();
        reputation.setCount(reputation.getCount() + voteQuestion.getVote().getValue());
        return reputation;
    }
}
