package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerUserDto;

import java.util.List;

public interface AnswerUserDtoDao {
    List<AnswerUserDto> getAnswerForLastWeek();
}