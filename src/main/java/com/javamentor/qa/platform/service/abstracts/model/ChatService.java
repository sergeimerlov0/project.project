package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.Chat;
import org.springframework.stereotype.Service;


@Service
public interface ChatService extends ReadWriteService<Chat, Long>{
}
