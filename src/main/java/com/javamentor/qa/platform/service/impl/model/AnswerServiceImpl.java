package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerServiceImpl extends ReadWriteServiceImpl<Answer, Long> implements AnswerService {

    private final AnswerDao answerDao;

    @Autowired
    public AnswerServiceImpl(AnswerDao answerDao) {
        super(answerDao);

        this.answerDao = answerDao;
    }

    @Override
    @Transactional
    public Boolean checkAnswerByQuestionIdAndUserId(Long questionId, Long userId) {
        return answerDao.getListAnswerByQuestionIdAndUserId(questionId, userId).size()>0;
    }


}
