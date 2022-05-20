package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerUserDto;

import java.util.List;

public interface AnswerUserDtoService {
    List<AnswerUserDto> getAnswerForLastWeek();
}