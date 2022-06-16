package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.GroupChatDAO;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import org.springframework.stereotype.Repository;

@Repository
public class GroupChatDAOImpl extends ReadWriteDaoImpl<GroupChat, Long> implements GroupChatDAO {
}
