package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;

import java.util.List;

public interface AnswerDtoDao {

    public List<AnswerDto> getAnswerByQuestionId(Long id);

    List<AnswerDto> getAnswerByQuestionId2(Long id);
}
