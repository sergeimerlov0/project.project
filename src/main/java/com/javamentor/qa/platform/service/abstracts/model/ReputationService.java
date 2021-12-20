package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;

public interface ReputationService extends ReadWriteService<Reputation, Long>{
    Reputation setReputationCount (VoteQuestion voteQuestion, Long questionId);
}
