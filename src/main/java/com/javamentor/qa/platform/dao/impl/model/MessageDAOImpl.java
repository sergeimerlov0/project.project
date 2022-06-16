package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.MessageDAO;
import com.javamentor.qa.platform.models.entity.chat.Message;
import org.springframework.stereotype.Repository;

@Repository
public class MessageDAOImpl extends ReadWriteDaoImpl<Message, Long> implements MessageDAO {
}
