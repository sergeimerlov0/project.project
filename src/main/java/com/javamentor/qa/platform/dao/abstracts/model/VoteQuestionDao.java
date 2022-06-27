package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;

import java.util.Optional;

public interface VoteQuestionDao extends ReadWriteDao<VoteQuestion, Long> {
    Integer getTotalVoteQuestionsByQuestionId(Long questionId);
    boolean userVoteCheck(Long questionId, Long userId);
    Optional<VoteQuestion> getByUserIdAndQuestionId(Long userId, Long questionId);
}