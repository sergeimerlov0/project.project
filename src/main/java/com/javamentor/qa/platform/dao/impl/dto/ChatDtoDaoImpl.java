package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ChatDtoDaoImpl implements ChatDtoDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<SingleChatDto> getAllSingleChatDto() {
       return entityManager.createQuery(
               "SELECT new com.javamentor.qa.platform.models.dto.SingleChatDto" +
                       "(c.id, " +
                       "c.userOne, " +
                       "c.useTwo) " +
                       "FROM SingleChat c JOIN User u ON c.userOne = u.id " +
                       "JOIN User u1 ON c.useTwo = u1.id" +
                       "WHERE c.chat.id = :chatId ", SingleChatDto.class)
               .getResultList();
    }

    }
