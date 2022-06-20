package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.GroupChatDto;

import java.util.Optional;

public interface ChatDtoDao {
    Optional<GroupChatDto> getGroupChatByChatId(Long chatId);
}
