package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("RegUser")
public class RegUserImpl implements PaginationDtoAble<UserDto> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.UserDto " +
                        "(user.id, " +
                        "user.email, " +
                        "user.fullName, " +
                        "user.imageLink, " +
                        "user.city, " +
                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = user.id), " +
                        "user.persistDateTime) " +
                        "FROM User user " +
                        "WHERE user.isEnabled = true " +
                        "AND (LOWER(user.fullName) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(user.email) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
                        "GROUP BY user.id " +
                        "ORDER BY user.persistDateTime",
                        UserDto.class)
                .setParameter("filter", param.get("filter"))
                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return ((Long) entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.isEnabled = true")
                .getSingleResult())
                .intValue();
    }
}