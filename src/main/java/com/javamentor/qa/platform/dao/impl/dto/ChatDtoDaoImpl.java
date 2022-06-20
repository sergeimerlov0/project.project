package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class ChatDtoDaoImpl implements ChatDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<GroupChatDto> getGroupChatByChatId(Long chatId) {
        Optional<GroupChatDto> o = SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.GroupChatDto" +
                                "(c.id, " +
                                "c.title, " +
                                "c.persistDate) " +
                                "FROM Chat c " +
                                "WHERE c.id = :chatId ", GroupChatDto.class)
                .setParameter("chatId", chatId));
        return o;
    }
}
