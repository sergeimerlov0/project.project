package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.service.abstracts.model.QuestionViewedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionViewedServiceImpl extends ReadWriteServiceImpl<QuestionViewed, Long> implements QuestionViewedService {
    private final QuestionViewedDao questionViewedDao;

    @Autowired
    public QuestionViewedServiceImpl (QuestionViewedDao questionViewedDao) {
        super(questionViewedDao);
        this.questionViewedDao = questionViewedDao;
    }

    @Override
    public boolean questionViewCheckByUserIdAndQuestionId (Long questionId, String email) {
        return questionViewedDao.questionViewCheckByUserIdAndQuestionId(questionId, email);
    }
}