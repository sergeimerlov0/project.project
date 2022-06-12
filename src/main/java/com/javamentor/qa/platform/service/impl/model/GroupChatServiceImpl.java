package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.GroupChatDAO;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import com.javamentor.qa.platform.service.abstracts.model.GroupChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupChatServiceImpl extends ReadWriteServiceImpl<GroupChat, Long> implements GroupChatService {

    @Autowired
    public GroupChatServiceImpl (GroupChatDAO groupChatDAO) {
        super(groupChatDAO);
    }

}
