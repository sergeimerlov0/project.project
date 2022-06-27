package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ChatDao;
import com.javamentor.qa.platform.models.entity.chat.Chat;
import com.javamentor.qa.platform.service.abstracts.model.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl extends ReadWriteServiceImpl<Chat, Long> implements ChatService {


    public final ChatDao chatDao;

    @Autowired
    public ChatServiceImpl(ChatDao chatDao) {
        super(chatDao);
        this.chatDao = chatDao;
    }
}
