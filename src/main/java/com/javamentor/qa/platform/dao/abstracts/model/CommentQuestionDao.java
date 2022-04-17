package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.Comment;
import com.javamentor.qa.platform.models.entity.question.Question;

public interface CommentQuestionDao extends ReadWriteDao<Comment, Long>{
    @Override
    void persist(Comment comment);
}
