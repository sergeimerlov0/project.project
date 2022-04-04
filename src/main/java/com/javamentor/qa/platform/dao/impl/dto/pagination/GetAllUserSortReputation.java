package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("AllUsersSortedByReputation")
@RequiredArgsConstructor
public class GetAllUserSortReputation implements PaginationDtoAble<UserDto> {
@PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.UserDto" +
                        "(u.id, " +
                        "u.email, " +
                        "u.fullName, " +
                        "u.imageLink, " +
                        "u.city, " +
                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = u.id) AS rCount, " +
                        "u.persistDateTime)" +
                        "FROM User u " +
                        "GROUP BY u.id " +
                        "ORDER BY rCount DESC")
                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return ((Long) entityManager.createQuery("SELECT COUNT(u) FROM  User u")
                .getSingleResult())
                .intValue();
    }
}
