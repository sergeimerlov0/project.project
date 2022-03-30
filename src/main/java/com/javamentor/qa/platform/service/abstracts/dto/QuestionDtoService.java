package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import java.util.List;
import java.util.Optional;

public interface QuestionDtoService extends PaginationServiceDto<QuestionViewDto> {
    Optional<QuestionDto> getQuestionDtoByQuestionId(Long id);
    List<QuestionCommentDto> getQuestionCommentByQuestionId(Long id);
}