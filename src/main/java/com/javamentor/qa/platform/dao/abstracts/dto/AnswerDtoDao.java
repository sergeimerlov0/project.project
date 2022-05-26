package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import java.util.List;
import java.util.Optional;

public interface AnswerDtoDao {
    List<AnswerDto> getAnswerByQuestionId(Long id);
    Optional<AnswerDto> getAnswerDtoById(Long answerId);
    Integer getCountOfAnswersByUser(long userId);
}