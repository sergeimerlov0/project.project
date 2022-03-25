package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.Bookmarks;

import java.util.List;

public interface BookmarkService extends ReadWriteService<Bookmarks, Long>{
    List<Long> bookmarkByUserId(Long userId);

    boolean questionIsPresentInTheListOfUser(Long userId, Long questionId);
}