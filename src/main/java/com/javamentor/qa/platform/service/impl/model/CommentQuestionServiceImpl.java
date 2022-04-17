package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.CommentQuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.Comment;
import com.javamentor.qa.platform.service.abstracts.model.CommentQuestionService;
import org.springframework.stereotype.Service;

@Service
public class CommentQuestionServiceImpl extends ReadWriteServiceImpl<Comment, Long> implements CommentQuestionService {
private CommentQuestionDao commentQuestionDao;
    public CommentQuestionServiceImpl(ReadWriteDao<Comment, Long> commentLongReadWriteDao) {
        super(commentLongReadWriteDao);
    }
}
