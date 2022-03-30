package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.transaction.annotation.Transactional;

public interface AnswerService extends ReadWriteService<Answer, Long> {
    @Transactional
    Boolean checkAnswerByQuestionIdAndUserId(Long questionId, Long userId);
}