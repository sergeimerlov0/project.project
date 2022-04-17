package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;
import org.springframework.transaction.annotation.Transactional;

public interface CommentAnswerService extends ReadWriteService<CommentAnswer, Long> {
    @Transactional
    Boolean checkCommentAnswerByQuestionIdAndUserId(Long questionId, Long userId);
}