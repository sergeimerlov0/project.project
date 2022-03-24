package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import java.util.List;

public interface IgnoredTagDao extends ReadWriteDao<IgnoredTag, Long> {
    List<Long> ignoredTagsByUserId(Long userId);
    boolean tagIsPresentInTheListOfUser(Long userId, Long tagId);
}