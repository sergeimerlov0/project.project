package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.Comment;

public interface CommentQuestionService extends ReadWriteService<Comment, Long> {
    @Override
    void persist(Comment comment);
}
