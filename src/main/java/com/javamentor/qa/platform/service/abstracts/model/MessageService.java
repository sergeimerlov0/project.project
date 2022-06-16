package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.Message;
import org.springframework.stereotype.Service;

@Service
public interface MessageService extends ReadWriteService<Message, Long>{
}
