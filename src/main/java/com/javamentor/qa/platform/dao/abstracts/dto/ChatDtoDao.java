package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.SingleChatDto;

import java.util.List;

public interface ChatDtoDao {
    List<SingleChatDto> getAllSingleChatDto();
}
