package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("RegaUser")
public class RegaUserImpl implements PaginationDtoAble<UserDto> {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDto> getItems(Map<String, Object> param) {
        List<UserDto> dtoList = entityManager.createQuery("SELECT new  com.javamentor.qa.platform.models.dto.UserDto" +
                        "(e.id,e.email,e.fullName,e.imageLink,e.city,sum(r.count))" +
                        "FROM User e left outer JOIN Reputation r on (e.id=r.author.id)" +
                        " where e.isEnabled=true " +
                        "group by e.id ORDER BY e.persistDateTime  ", UserDto.class)
                .getResultList();

        return dtoList;
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return getItems(param).size();
    }


}
