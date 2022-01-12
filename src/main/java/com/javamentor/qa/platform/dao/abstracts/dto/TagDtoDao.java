package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import java.util.List;
import java.util.Map;

public interface TagDtoDao {

    List<TagDto> getTagsByQuestionId(Long id);
    List<TagDto> getTrackedTagById(Long id);
    List<TagDto> getIgnoreTagById(Long id);
    List<RelatedTagsDto> getRelatedTagsDto();
    Map<Long, List<TagDto>> getMapTagsByQuestionIds(List<Long> questionIds);
}
