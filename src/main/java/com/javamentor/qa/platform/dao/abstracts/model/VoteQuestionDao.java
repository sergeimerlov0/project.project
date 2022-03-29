package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;

public interface VoteQuestionDao extends ReadWriteDao<VoteQuestion, Long> {
    Integer getTotalVoteQuestionsByQuestionId(Long questionId);
    boolean userVoteCheck(Long questionId, Long userId);
}