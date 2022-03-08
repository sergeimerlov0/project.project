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
        int page = Integer.parseInt(String.valueOf(param.get("page")));
        int items = Integer.parseInt(String.valueOf(param.get("items")));
        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.UserDto " +
                        "(e.id, e.email, e.fullName, e.imageLink, e.city, " +
                        "SUM(r.count), e.persistDateTime) " +
                        "FROM User e LEFT OUTER JOIN Reputation r ON (e.id=r.author.id)" +
                        "WHERE e.isEnabled = true AND (LOWER(e.fullName) LIKE LOWER(CONCAT('%', :filter, '%')) " +
                        "OR LOWER(e.email) LIKE LOWER(CONCAT('%', :filter, '%'))) " +
                        "GROUP BY e.id " +
                        "ORDER BY e.persistDateTime", UserDto.class)
                .setParameter("filter", param.get("filter"))
                .setFirstResult((page - 1) * items)
                .setMaxResults(items)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return ((Long) entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.isEnabled = true")
                .getSingleResult())
                .intValue();
    }
}