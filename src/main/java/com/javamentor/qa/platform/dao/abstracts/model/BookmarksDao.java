package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.Bookmarks;

import java.util.List;

public interface BookmarksDao extends ReadWriteDao<Bookmarks, Long>{
    List<Long> bookmarkByUserId(Long userId);

    boolean questionIsPresentInTheListOfUser(Long userId, Long questionId);
}
