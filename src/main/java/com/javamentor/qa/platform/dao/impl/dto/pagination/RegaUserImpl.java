package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserTestDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("RegaUser")
public class RegaUserImpl implements PaginationDtoAble<UserTestDto> {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserTestDto> getItems(Map<String, Object> param) {
        List<UserDto> dtoList = entityManager.createQuery("SELECT new  com.javamentor.qa.platform.models.dto.UserDto" +
                        "(e.id,e.email,e.fullName,e.imageLink,e.city,sum(r.count))" +
                        "FROM User e left outer JOIN Reputation r on (e.id=r.author.id)" +
                        " where e.isEnabled=true " +
                        "group by e.id ORDER BY e.persistDateTime  ", UserDto.class)
                .getResultList();

        List<UserTestDto> result = new ArrayList<>();
        dtoList.forEach(userDto -> result.add(new UserTestDto(
                userDto.getId(),
                userDto.getEmail(),
                userDto.getFullName(),
                userDto.getLinkImage(),
                userDto.getCity(),
                userDto.getReputation()


        )));

        return result;
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return getItems(param).size();
    }


}
