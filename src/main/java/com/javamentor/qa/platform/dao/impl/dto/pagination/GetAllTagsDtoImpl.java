package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("TagsSortedByName")
public class GetAllTagsDtoImpl implements PaginationDtoAble<TagDto> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.TagDto " +
                        "(t.id, t.name, t.description) " +
                        "FROM Tag t " +
                        "ORDER BY t.name ASC",
                        TagDto.class)
                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return ((Long) entityManager.createQuery("SELECT COUNT(t) FROM Tag t")
                .getSingleResult())
                .intValue();
    }
}