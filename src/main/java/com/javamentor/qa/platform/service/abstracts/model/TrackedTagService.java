package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import java.util.List;

public interface TrackedTagService extends ReadWriteService<TrackedTag, Long> {
    List<Long> trackedTagsByUserId(Long userId);
    boolean tagIsPresentInTheListOfUser(Long userId, Long tagId);
}