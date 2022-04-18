package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.BookmarksDto;

import java.util.List;

public interface BookmarksDtoService {
    List<BookmarksDto> getBookmarksDtoByUserId(Long userId);
}
