package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;

import java.util.List;

public interface TagDtoService extends PaginationServiceDto<TagDto> {

    List<TagDto> getTagsByQuestionId(Long id);

    List<TagDto> getTrackedTagById(Long id);

    List<TagDto> getIgnoreTagById(Long id);

    List<RelatedTagsDto> getRelatedTagsDto();

    List<TagDto> getTagsTop10WithString(String partTag);
}
