package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Rustam
 */

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerDtoServiceImpl implements AnswerDtoService {

    private final AnswerDtoDao answerDtoDao;

    @Override
    public List<AnswerDto> getAnswerByQuestionId2(Long id) {
        return answerDtoDao.getAnswerByQuestionId2(id);
    }

    @Override
    public List<AnswerDto> getAnswerByQuestionId(Long id) {
        return null;
    }

    @Override
    @Transactional
    public AnswerDto getAnswerByQuestionIdAndUserIdAndAnswerBody(Long questionId, Long userId, String htmlBody) {
        List<AnswerDto> list = answerDtoDao.getAnswerByQuestionId2(questionId)
                .stream()
                .filter(x -> Objects.equals(x.getBody(), htmlBody)
                        && (Objects.equals(x.getUserId(), userId)))
                .collect(Collectors.toList());
        return list.get(0);
    }
}
