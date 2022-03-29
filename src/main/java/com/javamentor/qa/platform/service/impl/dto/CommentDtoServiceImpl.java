package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.service.abstracts.dto.CommentDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentDtoServiceImpl implements CommentDtoService {
    private final CommentDtoDao commentDtoDao;

    @Override
    public List<CommentDto> getCommentDtosByQuestionId(Long id) {
        return commentDtoDao.getCommentDtosByQuestionId(id);
    }

    @Override
    public Map<Long, List<CommentDto>> getMapCommentDtosByQuestionIds(List<Long> ids) {
        return commentDtoDao.getMapCommentDtosByQuestionIds(ids);
    }
}