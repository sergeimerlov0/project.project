package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.CommentAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;
import com.javamentor.qa.platform.service.abstracts.model.CommentAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentAnswerServiceImpl extends ReadWriteServiceImpl<CommentAnswer, Long> implements CommentAnswerService {
    private final CommentAnswerDao commentAnswerDao;

    @Autowired
    public CommentAnswerServiceImpl(CommentAnswerDao commentAnswerDao) {
        super(commentAnswerDao);
        this.commentAnswerDao = commentAnswerDao;
    }

    @Override
    @Transactional
    public Boolean checkCommentAnswerByQuestionIdAndUserId(Long questionId, Long userId) {
        return commentAnswerDao.checkCommentAnswerByQuestionIdAndUserId(questionId, userId);
    }
}