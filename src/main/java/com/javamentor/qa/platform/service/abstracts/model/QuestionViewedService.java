package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import org.springframework.cache.annotation.Cacheable;

public interface QuestionViewedService extends ReadWriteService<QuestionViewed, Long> {

    boolean questionViewCheckByUserIdAndQuestionId (Long questionId, Long userId);

    @Override
    void persist (QuestionViewed questionViewed);
}
