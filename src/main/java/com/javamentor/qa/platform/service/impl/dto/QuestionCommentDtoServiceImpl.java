package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionCommentDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionCommentDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionCommentDtoServiceImpl implements QuestionCommentDtoService {

    private final QuestionCommentDtoDao questionCommentDtoDao;

    @Override
    @Transactional
    public List<QuestionCommentDto> getQuestionCommentByQuestionId(Long id) {
        return questionCommentDtoDao.getQuestionCommentByQuestionId(id);
    }
}
