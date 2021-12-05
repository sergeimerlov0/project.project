package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserDtoDaoImpl implements UserDtoDao {
    @PersistenceContext()
    private EntityManager entityManager;


    @Override
    public UserDto getUserById(Long id) {

        return entityManager.createQuery("SELECT new  com.javamentor.qa.platform.models.dto.UserDto" +
                        "(e.id,e.email,e.fullName,e.imageLink,e.city,r.count)" +
                        "FROM User e left outer JOIN Reputation r on (e.id=r.author.id)" +
                        " where e.id =:id and e.isEnabled=true ", UserDto.class)
                .setParameter("id", id)
                .getSingleResult();


    }
//    @Override
//    JOIN Reputation r on (e.id=r.author.id)
//    public UserDto getUserById(Long id) {
//
//        return entityManager.createQuery("SELECT new  com.javamentor.qa.platform.models.dto.UserDto" +
//                        "(e.author.id,e.author.email,e.author.fullName,e.author.imageLink,e.author.city,e.count)" +
//                        "FROM Reputation e" +
//                        " where e.author.id =:id", UserDto.class)
//                .setParameter("id",id)
//                .getSingleResult();
//
//
//    }
}
