package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionDtoServiceIml extends PaginationServiceDtoImpl<QuestionDto> implements QuestionDtoService {
    private final QuestionDtoDao questionDtoDao;
    private final TagDtoDao tagDtoDao;

    @Override
    public Optional<QuestionDto> getQuestionDtoByQuestionId(Long id) {
        Optional<QuestionDto> questionDto = questionDtoDao.getQuestionDtoByQuestionId(id);
        questionDto.ifPresent(dto -> dto.setListTagDto(tagDtoDao.getTagsByQuestionId(id)));
        return questionDto;
    }

    @Override
    @Transactional
    public List<QuestionCommentDto> getQuestionCommentByQuestionId(Long id) {
        return questionDtoDao.getQuestionCommentByQuestionId(id);
    }
}
