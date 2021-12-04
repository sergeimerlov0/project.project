package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerDao extends ReadWriteDao<Answer, Long> {
}