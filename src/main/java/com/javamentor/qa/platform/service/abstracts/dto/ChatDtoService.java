package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.GroupChatDto;

import java.util.Optional;

public interface ChatDtoService {
    Optional<GroupChatDto> getGroupChatByChatId(Long chatId);
}
