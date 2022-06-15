package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.service.abstracts.dto.ChatDtoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class ChatDtoServiceImpl implements ChatDtoService {

    private final ChatDtoDao chatDtoDao;


    public ChatDtoServiceImpl(ChatDtoDao chatDtoDao) {
        this.chatDtoDao = chatDtoDao;
    }

    @Override
    public Optional<GroupChatDto> getGroupChatByChatId(Long chatId) {
        return chatDtoDao.getGroupChatByChatId(chatId);
    }
}
