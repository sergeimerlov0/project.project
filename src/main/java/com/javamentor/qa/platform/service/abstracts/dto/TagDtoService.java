package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;

import java.util.List;

public interface TagDtoService {

    List<TagDto> getTagsByQuestionId(Long id);

    List<TagDto> getTrackedTagById(Long id);

    List<TagDto> getTrackedTagsByIds(Iterable<Long> ids);

    List<TagDto> getIgnoreTagById(Long id);

    List<TagDto> getIgnoreTagsByIds(Iterable<Long> ids);

    List<RelatedTagsDto> getRelatedTagsDto();
}
