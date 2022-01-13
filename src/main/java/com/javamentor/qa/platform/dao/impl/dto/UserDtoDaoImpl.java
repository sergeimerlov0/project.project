package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class UserDtoDaoImpl implements UserDtoDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<UserDto> getUserById(Long id) {

        return entityManager.createQuery("SELECT new  com.javamentor.qa.platform.models.dto.UserDto" +
                        "(e.id,e.email,e.fullName,e.imageLink,e.city,sum(r.count),e.persistDateTime)" +
                        "FROM User e left outer JOIN Reputation r on (e.id=r.author.id)" +
                        " where e.id =:id and e.isEnabled=true " +
                        "group by e.id", UserDto.class)
                 .setParameter("id", id).getResultStream().findAny();

    }

}

