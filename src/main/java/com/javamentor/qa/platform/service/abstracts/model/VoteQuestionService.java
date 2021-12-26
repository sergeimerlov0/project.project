package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;

import java.util.List;

public interface VoteQuestionService extends ReadWriteService<VoteQuestion, Long> {

    List<VoteQuestion> getAllVoteQuestionsByQuestionId(Long questionId);

    boolean userVoteCheck(Long questionId, Long userId);
}
