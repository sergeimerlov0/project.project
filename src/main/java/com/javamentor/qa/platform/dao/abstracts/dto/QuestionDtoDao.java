package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import java.util.List;
import java.util.Optional;

public interface QuestionDtoDao {
    Optional<QuestionDto> getQuestionDtoByQuestionId(Long id);
    List<QuestionCommentDto> getQuestionCommentByQuestionId(Long id);
}