package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionDtoServiceIml implements QuestionDtoService {
    private final QuestionDtoDao questionDtoDao;
    private final TagDtoDao tagDtoDao;

    @Override
    @Transactional
    public Optional<QuestionDto> getQuestionDtoByQuestionId(Long id) {
        Optional<QuestionDto> questionDto = questionDtoDao.getQuestionDtoByQuestionId(id);
        questionDto.ifPresent(dto -> dto.setListTagDto(tagDtoDao.getTagsByQuestionId(id)));
        return questionDto;
    }
}
