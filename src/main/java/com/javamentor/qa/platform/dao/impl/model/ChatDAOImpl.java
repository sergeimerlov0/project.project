package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ChatDAO;
import com.javamentor.qa.platform.models.entity.chat.Chat;
import org.springframework.stereotype.Repository;

@Repository
public class ChatDAOImpl extends ReadWriteDaoImpl<Chat, Long> implements ChatDAO {
}
