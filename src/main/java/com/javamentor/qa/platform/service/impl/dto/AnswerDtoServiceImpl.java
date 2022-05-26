package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.dao.impl.dto.UserDtoDaoImpl;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserDtoDaoImpl userDtoDaoImpl;

    @Override
    public List<AnswerDto> getAnswerByQuestionId(Long id) {
        return answerDtoDao.getAnswerByQuestionId(id);
    }

    @Override
    public Optional<AnswerDto> getAnswerDtoByAnswerId(Long answerId) {
        return answerDtoDao.getAnswerDtoById(answerId);
    }

    @Override
    public Integer getCountOfAnswersByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userDtoDaoImpl.getUserIdByEmail((String) authentication.getPrincipal());
        return answerDtoDao.getCountOfAnswersByUser(userId);
    }
}