package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionCommentDto;

import java.util.List;

public interface QuestionCommentDtoService {
    List<QuestionCommentDto> getQuestionCommentByQuestionId(Long id);
}
