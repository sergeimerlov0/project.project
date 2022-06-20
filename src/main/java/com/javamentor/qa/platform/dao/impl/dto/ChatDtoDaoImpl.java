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
    public List<SingleChatDto> getAllSingleChatDto(Long userId) {
       return entityManager.createQuery(
               "SELECT new com.javamentor.qa.platform.models.dto.SingleChatDto" +
                       "(c.id, " +
                       "CASE WHEN c.userOne <> :userId THEN u.fullName WHEN c.useTwo <> :userID THEN  u.fullName END, " +
                       "CASE WHEN c.userOne <> :userId THEN u.imageLink WHEN c.useTwo <> :userID THEN  u.imageLink END, " +
                       "m.message, " +
                       "m.persistDate) " +
                       "FROM SingleChat c LEFT JOIN User u ON c.useTwo = u.id " +
                       "LEFT JOIN Message m ON c.chat.id = m.chat.id " +
                       "WHERE c.userOne = :user ", SingleChatDto.class)
               .setParameter("userId", userId)
               .getResultList();
    }

    }
