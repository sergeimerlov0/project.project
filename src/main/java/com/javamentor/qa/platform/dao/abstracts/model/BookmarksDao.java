package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.Bookmarks;

import java.util.List;

public interface BookmarksDao extends ReadWriteDao<Bookmarks, Long>{
    List<Long> bookmarkedQuestionByUserId(Long userId);

    boolean bookmarkExistsByQuestionIdByUserId(Long userId, Long questionId);
}
