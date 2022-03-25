package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.BookmarksDao;
import com.javamentor.qa.platform.models.entity.Bookmarks;
import com.javamentor.qa.platform.service.abstracts.model.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkServiceImpl extends ReadWriteServiceImpl<Bookmarks, Long> implements BookmarkService {
    private final BookmarksDao bookmarksDao;

    @Autowired
    public BookmarkServiceImpl(BookmarksDao bookmarksDao) {
        super(bookmarksDao);
        this.bookmarksDao = bookmarksDao;
    }

    @Override
    public List<Long> bookmarkByUserId(Long userId) {
        return bookmarksDao.bookmarkByUserId(userId);
    }

    @Override
    public boolean questionIsPresentInTheListOfUser(Long userId, Long questionId) {
        return bookmarksDao.questionIsPresentInTheListOfUser(userId, questionId);
    }
}
