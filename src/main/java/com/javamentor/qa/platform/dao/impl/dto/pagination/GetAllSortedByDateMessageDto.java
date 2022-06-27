package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("SortedByDateMessageDto")
@RequiredArgsConstructor
public class GetAllSortedByDateMessageDto implements PaginationDtoAble<MessageDto> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MessageDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        long chatId = (long) param.get("chatId");
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.MessageDto" +
                                "(m.id, " +
                                "m.message, " +
                                "m.userSender.nickname, " +
                                "m.userSender.id, " +
                                "m.chat.id, " +
                                "m.userSender.imageLink, " +
                                "m.persistDate) " +
                                "FROM Message m " +
                                "WHERE m.chat.id = :chatId " +
                                "ORDER BY m.persistDate DESC", MessageDto.class)
                .setParameter("chatId", chatId)
                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return ((Long) entityManager.createQuery("SELECT COUNT(m) FROM Message m")
                .getSingleResult())
                .intValue();
    }
}
