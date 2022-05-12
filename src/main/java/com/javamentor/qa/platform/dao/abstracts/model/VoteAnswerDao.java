package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;

import java.util.Optional;

public interface VoteAnswerDao extends ReadWriteDao<VoteAnswer, Long>{
    Long getTotalVotesByAnswerId(Long id);
    Optional<VoteAnswer> getByUserIdAndAnswerId(Long userId, Long answerId);
}