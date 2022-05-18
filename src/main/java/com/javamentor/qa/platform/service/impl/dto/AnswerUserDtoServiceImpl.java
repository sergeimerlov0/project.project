package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerUserDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerUserDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerUserDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Rustam
 */

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerUserDtoServiceImpl implements AnswerUserDtoService {
    private final AnswerUserDtoDao answerUserDtoDao;

    @Override
    public List<AnswerUserDto> getAnswerByQuestionIdForLastWeek() {
        return answerUserDtoDao.getAnswerByQuestionIdForLastWeek();
    }

}