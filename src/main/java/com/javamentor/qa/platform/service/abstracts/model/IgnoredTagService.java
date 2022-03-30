package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import java.util.List;

public interface IgnoredTagService extends ReadWriteService<IgnoredTag, Long> {
    List<Long> ignoredTagsByUserId(Long userId);
    boolean tagIsPresentInTheListOfUser(Long userId, Long tagId);
}