package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.service.abstracts.model.QuestionViewedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionViewedServiceImpl extends ReadWriteServiceImpl<QuestionViewed, Long> implements QuestionViewedService {

    private final QuestionViewedDao questionViewedDao;

    @Autowired
    public QuestionViewedServiceImpl (QuestionViewedDao questionViewedDao) {
        super(questionViewedDao);
        this.questionViewedDao = questionViewedDao;
    }

    @Override
    public boolean questionViewCheckByUserIdAndQuestionId (Long questionId, Long userId) {
        return questionViewedDao.questionViewCheckByUserIdAndQuestionId(questionId, userId);
    }

    @Override
    //@CacheEvict("questionViewCheckByUserIdAndQuestionId")    //переопределил метод для того, чтобы после добавления в БД очищался кэш с проверкой
    public void persist (QuestionViewed questionViewed) {
        super.persist(questionViewed);
    }

}
