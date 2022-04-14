package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.BookmarksDtoDao;
import com.javamentor.qa.platform.models.dto.BookmarksDto;
import com.javamentor.qa.platform.service.abstracts.dto.BookmarksDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarksDtoServiceImpl implements BookmarksDtoService {
    private final BookmarksDtoDao bookmarksDtoDao;

    @Override
    public List<BookmarksDto> getBookmarksDtoByUserId(Long userId) {
        return bookmarksDtoDao.getBookmarksDtoByUserId(userId);
    }
}
