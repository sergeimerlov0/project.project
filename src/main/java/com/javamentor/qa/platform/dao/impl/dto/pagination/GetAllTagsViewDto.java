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
        LocalDateTime oneDay = LocalDateTime.now().minusDays(1L);
        LocalDateTime weekDays = LocalDateTime.now().minusDays(7L);
        int currentPageNumber = Integer.parseInt(String.valueOf(param.get("currentPageNumber")));
        int itemsOnPage = Integer.parseInt(String.valueOf(param.get("itemsOnPage")));
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto." +
                        "TagViewDto(tag.id, tag.name, tag.description, " +
                        "(select count (ques.id) from Question ques), " +
                        "(select count (ques.id) from Question ques where ques.persistDateTime between :day and current_date )," +
                        "(select count (ques.id) from Question ques where ques.persistDateTime between :week and current_date))" +
                        "from Tag tag " +
                        "join tag.questions as question" +
                        "and WHERE (lower(tag.name) LIKE lower(CONCAT('%', :filter, '%')))" +
                        "group by tag.id" , TagViewDto.class)
                .setParameter("filter", param.get("filter"))
                .setParameter("day", oneDay)
                .setParameter("week", weekDays)
                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }
    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return ((Long) entityManager.createQuery(
                        "select count(t) from Tag t"
                )
                .getSingleResult()).intValue();
    }
}
