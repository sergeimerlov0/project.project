package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.CommentDto;
import java.util.List;
import java.util.Map;

public interface CommentDtoDao {
    List<CommentDto> getCommentDtosByQuestionId(Long id);
    Map<Long, List<CommentDto>> getMapCommentDtosByQuestionIds(List<Long> ids);
}