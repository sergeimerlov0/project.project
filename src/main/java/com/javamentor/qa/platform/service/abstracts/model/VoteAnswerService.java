package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface VoteAnswerService extends ReadWriteService<VoteAnswer, Long> {

    Long getTotalVotesByAnswerId(Long id);

    ResponseEntity<Long> postVoteUp(Long answerId, Principal principal);

    ResponseEntity<Long> postVoteDown(Long answerId, Principal principal);
}
