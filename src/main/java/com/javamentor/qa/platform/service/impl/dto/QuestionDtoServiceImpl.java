package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionDtoServiceImpl extends PaginationServiceDtoImpl<QuestionDto> implements QuestionDtoService {
    private final QuestionDtoDao questionDtoDao;
    private final TagDtoDao tagDtoDao;

    @Override
    @Transactional
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

    @Override
    @Transactional
    public PageDto<QuestionDto> getPageDto(int currentPageNumber, int itemsOnPage, Map<String, Object> map) {
        PageDto<QuestionDto> pageDto = super.getPageDto(currentPageNumber, itemsOnPage, map);
        List<QuestionDto> questionDtoList = pageDto.getItems();
        List<Long> questionIds = questionDtoList.stream()
                .map(QuestionDto::getId)
                .collect(Collectors.toList());
        Map<Long, List<TagDto>> tagsMap = tagDtoDao.getMapTagsByQuestionIds(questionIds);
        for (QuestionDto questionDto : questionDtoList) {
            questionDto.setListTagDto(tagsMap.get(questionDto.getId()));
        }
        pageDto.setItems(questionDtoList);
        return pageDto;
    }
}
