package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;

import java.util.List;

public interface VoteQuestionDao extends ReadWriteDao<VoteQuestion, Long> {

    List<VoteQuestion> getAllVoteQuestionsByQuestionId(Long questionId);

    boolean userVoteCheck(Long questionId, Long userId);
}
