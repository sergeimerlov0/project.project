package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnswerDtoService {

    List<AnswerDto> getAnswerByQuestionId2(Long id);

    public List<AnswerDto> getAnswerByQuestionId(Long id);

    @Transactional
    AnswerDto getAnswerByQuestionIdAndUserIdAndAnswerBody(Long questionId, Long userId, String htmlBody);
}
