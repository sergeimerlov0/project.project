package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionDtoServiceImpl extends PaginationServiceDtoImpl<QuestionViewDto> implements QuestionDtoService {
    private final QuestionDtoDao questionDtoDao;
    private final TagDtoDao tagDtoDao;
    private final CommentDtoDao commentDtoDao;

    @Override
    @Transactional
    public Optional<QuestionDto> getQuestionDtoByQuestionId(Long id) {
        Optional<QuestionDto> questionDto = questionDtoDao.getQuestionDtoByQuestionId(id);
        questionDto.ifPresent(dto -> dto.setListTagDto(tagDtoDao.getTagsByQuestionId(id)));
        questionDto.ifPresent((dto -> dto.setComments(commentDtoDao.getCommentDtosByQuestionId(id))));
        return questionDto;
    }

    @Override
    @Transactional
    public List<QuestionCommentDto> getQuestionCommentByQuestionId(Long id) {
        return questionDtoDao.getQuestionCommentByQuestionId(id);
    }

    @Override
    @Transactional
    public PageDto<QuestionViewDto> getPageDto(int currentPageNumber, int itemsOnPage, Map<String, Object> map) {
        PageDto<QuestionViewDto> pageDto = super.getPageDto(currentPageNumber, itemsOnPage, map);
        List<QuestionViewDto> questionViewDtoList = pageDto.getItems();

        List<Long> questionIds = questionViewDtoList.stream()
                .map(QuestionViewDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<TagDto>> tagsMap = tagDtoDao.getMapTagsByQuestionIds(questionIds);
        for (QuestionViewDto questionViewDto : questionViewDtoList) {
            questionViewDto.setListTagDto(tagsMap.get(questionViewDto.getId()));
        }
        pageDto.setItems(questionViewDtoList);
        return pageDto;
    }
}