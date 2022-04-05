package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.BookMarks;

import java.util.List;

public interface BookmarkService extends ReadWriteService<BookMarks, Long>{
    List<Long> bookmarkByUserId(Long userId);

    boolean bookmarkExistsByQuestionIdByUserId(Long userId, Long questionId);
}