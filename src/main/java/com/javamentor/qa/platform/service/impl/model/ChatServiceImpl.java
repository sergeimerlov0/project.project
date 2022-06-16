package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ChatDAO;
import com.javamentor.qa.platform.models.entity.chat.Chat;
import com.javamentor.qa.platform.service.abstracts.model.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl extends ReadWriteServiceImpl<Chat, Long> implements ChatService {


    public final ChatDAO chatDAO;

    @Autowired
    public ChatServiceImpl(ChatDAO chatDAO) {
        super(chatDAO);
        this.chatDAO = chatDAO;
    }
}
