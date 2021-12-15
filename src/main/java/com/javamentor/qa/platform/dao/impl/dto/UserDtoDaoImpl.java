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

        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.UserDto(rep.id, rep.author.email, rep.author.fullName,rep.author.imageLink,rep.author.city,rep.count)" +
                        " from Reputation rep where rep.author.id =: id", UserDto.class)
                .setParameter("id", id).getResultStream().findAny();

    }

}

