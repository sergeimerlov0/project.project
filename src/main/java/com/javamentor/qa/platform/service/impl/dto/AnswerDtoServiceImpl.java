package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerUserDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

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
    public List<AnswerDto> getAnswerByQuestionId(Long id) {
        return answerDtoDao.getAnswerByQuestionId(id);
    }

    @Override
    public Optional<AnswerDto> getAnswerDtoByAnswerId(Long answerId) {
        return answerDtoDao.getAnswerDtoById(answerId);
    }

    @Override
    public Integer getCountOfAnswersByUserToWeek(Long userId) {
        return answerDtoDao.getCountOfAnswersByUserToWeek(userId);
    }

    @Override
    public List<AnswerUserDto> getAnswerForLastWeek() {
        return answerDtoDao.getAnswerForLastWeek();
    }

    @Override
    public List<AnswerDto> getDeletedAnswersByUserId(Long userId) {
        return answerDtoDao.getDeletedAnswersByUserId(userId);
    }
}