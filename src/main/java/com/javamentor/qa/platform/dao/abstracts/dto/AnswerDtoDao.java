package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.dto.AnswerUserDto;

import java.util.List;
import java.util.Optional;

public interface AnswerDtoDao {
    List<AnswerDto> getAnswerByQuestionId(Long id);
    Optional<AnswerDto> getAnswerDtoById(Long answerId);
    List<AnswerUserDto> getAnswerForLastWeek();
    List<AnswerDto> getDeletedAnswersByUserId(Long userId);
    Integer getCountOfAnswersByUserToWeek(long userId);
}