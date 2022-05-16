package com.javamentor.qa.platform.service.abstracts.model;


import com.javamentor.qa.platform.models.entity.question.CommentQuestion;
import org.springframework.stereotype.Service;

@Service
public interface CommentQuestionService extends ReadWriteService<CommentQuestion, Long> {
}
