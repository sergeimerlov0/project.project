package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.service.abstracts.model.QuestionViewedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuestionViewedServiceImpl extends ReadWriteServiceImpl<QuestionViewed, Long> implements QuestionViewedService {

    private final QuestionViewedDao questionViewedDao;

    @Autowired
    public QuestionViewedServiceImpl (QuestionViewedDao questionViewedDao) {
        super(questionViewedDao);
        this.questionViewedDao = questionViewedDao;
    }


    @Override
    @Cacheable(cacheNames = "questionViewResult", key = "#userId")
    public boolean questionViewCheckByUserIdAndQuestionId (Long questionId, Long userId) {
        return questionViewedDao.questionViewCheckByUserIdAndQuestionId(questionId, userId);
    }

    @Override
    public QuestionViewed getTotalQuestionViewByQuestionIAndUserId (Long questionId, Long userId) {
        return questionViewedDao.getTotalQuestionViewByQuestionIAndUserId(questionId, userId);
    }

    @Override
    public void persist (QuestionViewed questionViewed) {
        questionViewedDao.persist(questionViewed);
    }
}
