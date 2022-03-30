package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.QuestionViewed;

public interface QuestionViewedService extends ReadWriteService<QuestionViewed, Long> {
    boolean questionViewCheckByUserIdAndQuestionId (Long questionId, String email);
}