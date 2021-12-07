package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.entity.question.Tag;

import java.util.List;

public interface TagDtoDao {
    List<Tag> getTrackedTagByUsername(String username);

    List<Tag> getIgnoreTagByUsername(String username);
}
