package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;

public interface VoteQuestionService extends ReadWriteService<VoteQuestion, Long> {
    Integer getTotalVoteQuestionsByQuestionId(Long questionId);
    boolean userVoteCheck(Long questionId, Long userId);
    @Override
    void persist(VoteQuestion voteQuestion);
}