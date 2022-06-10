package com.javamentor.qa.platform.dao.impl.dto.pagination;

//import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("MessageDtoSortedByDate")
@RequiredArgsConstructor
public class MessageSortDate implements PaginationDtoAble<MessageDto> {
@PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MessageDto> getItems(Map<String, Object> param) {

        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        long userId = (long) param.get("userId");

        return entityManager.createQuery(
                "SELECT m.id, m.message, " +
                        "u.nickname, " +
                        "m.userSender, " +
                        "u.imageLink, " +
                        "m.persistDate " +
                        "FROM Message m LEFT JOIN User u ON m.userSender = u.id " +
                        "WHERE u.id = :id " +
                        "ORDER BY m.persistDate DESC", MessageDto.class)
                .setParameter("id", userId)
                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return ((Long) entityManager.createQuery("SELECT COUNT(m) FROM  Message m")
                .getSingleResult())
                .intValue();
    }

}