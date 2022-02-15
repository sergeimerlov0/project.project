package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;

import java.util.List;

public interface AnswerDtoService {

    List<AnswerDto> getAnswerByQuestionId(Long id);

    AnswerDto getAnswerDtoByAnswerAndReputation(Answer answer, Reputation reputation);

}
