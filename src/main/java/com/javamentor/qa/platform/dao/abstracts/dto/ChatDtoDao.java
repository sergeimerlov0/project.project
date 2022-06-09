package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;

import java.util.List;
import java.util.Map;

public interface ChatDtoDao {

//    List<MessageDto> getMessageSortedDate();

    List<MessageDto> getItems(Map<String, Object> param);
    int getTotalResultCount(Map<String, Object> param);
}