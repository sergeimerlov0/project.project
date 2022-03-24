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
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.UserDto " +
                        "(user.id, " +
                        "user.email, " +
                        "user.fullName, " +
                        "user.imageLink, " +
                        "user.city, " +
                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = user.id), " +
                        "user.persistDateTime) " +
                        "FROM User user " +
                        "WHERE user.id = :id " +
                        "AND user.isEnabled = true " +
                        "GROUP BY user.id",
                        UserDto.class)
                .setParameter("id", id)
                .getResultStream()
                .findAny();
    }
}