package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagDtoServiceImpl implements TagDtoService {

    private final TagDtoDao tagDtoDao;

    @Override
    @Transactional
    public List<TagDto> getTagsByQuestionId(Long id) {
        return tagDtoDao.getTagsByQuestionId(id);
    }

    @Override
    @Transactional
    public List<TagDto> getTrackedTagById(Long id) {
        return tagDtoDao.getTrackedTagById(id);
    }

    @Override
    @Transactional
    public List<TagDto> getIgnoreTagById(Long id) {
        return tagDtoDao.getIgnoreTagById(id);
    }

    @Override
    @Transactional
    public List<RelatedTagsDto> getRelatedTagsDto() {
        return tagDtoDao.getRelatedTagsDto();
    }
}