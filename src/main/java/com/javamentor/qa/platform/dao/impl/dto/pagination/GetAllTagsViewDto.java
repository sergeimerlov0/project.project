package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.TagViewDto;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository("TagsViewsSortedByName")
public class GetAllTagsViewDto implements PaginationDtoAble<TagViewDto> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagViewDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.TagViewDto" +
                        "(tag.id, " +
                        "tag.name, " +
                        "tag.description, " +
                        "(SELECT COUNT(ques.id) FROM Question ques), " +
                        "(SELECT COUNT(ques.id) FROM Question ques WHERE ques.persistDateTime BETWEEN :day AND current_date), " +
                        "(SELECT COUNT(ques.id) FROM Question ques WHERE ques.persistDateTime BETWEEN :week AND current_date)) " +
                        "FROM Tag tag " +
                        "JOIN tag.questions AS question " +
                        "WHERE (LOWER(tag.name) LIKE LOWER(CONCAT('%', :filter, '%')))" +
                        "GROUP BY tag.id " +
                        "ORDER BY tag.id ASC" ,
                        TagViewDto.class)
                .setParameter("filter", param.get("filter"))
                .setParameter("day", LocalDateTime.now().minusDays(1L))
                .setParameter("week", LocalDateTime.now().minusDays(7L))
                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }
    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return ((Long) entityManager.createQuery("SELECT COUNT(t) FROM Tag t")
                .getSingleResult()).intValue();
    }
}