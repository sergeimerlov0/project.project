package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.BookmarksDto;

import java.util.List;

public interface BookmarksDtoDao {
    List<BookmarksDto> getBookmarksDtoByUserId(Long userId);
}
