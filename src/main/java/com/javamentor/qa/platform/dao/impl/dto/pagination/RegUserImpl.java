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
        int page = (int) param.get("page");
        int items = Integer.parseInt(String.valueOf(param.get("items")));
        StringBuilder queryString = new StringBuilder(
                "SELECT new com.javamentor.qa.platform.models.dto.UserDto " +
                "(e.id, e.email, e.fullName, e.imageLink, e.city, " +
                "SUM(r.count), e.persistDateTime) " +
                "FROM User e LEFT OUTER JOIN Reputation r ON (e.id=r.author.id) " +
                "WHERE e.isEnabled = true ");
        if (param.containsKey("filter")) {
            queryString
                    .append("AND (LOWER(e.fullName) LIKE LOWER('%")
                    .append((String) param.get("filter"))
                    .append("%') OR LOWER(e.email) LIKE LOWER('%")
                    .append((String) param.get("filter"))
                    .append("%')) ");
        }
        queryString.append(
                "GROUP BY e.id " +
                "ORDER BY e.persistDateTime");
        return entityManager.createQuery(queryString.toString(), UserDto.class)
                .setFirstResult((page - 1) * items)
                .setMaxResults(items)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return ((Long) entityManager.createQuery("select count(u) from User u " +
                "where u.isEnabled=true").getSingleResult()).intValue();
    }
}