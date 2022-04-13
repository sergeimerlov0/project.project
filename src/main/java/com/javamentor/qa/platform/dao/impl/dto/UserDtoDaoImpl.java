package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
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

    @Override
    public List<UserProfileQuestionDto> getAllDeletedQuestionsByUserId(Long id) {
        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.UserProfileQuestionDto(" +
                                "q.id, " +
                                "q.title, " +
                                "(select (count(ans.id)) from Answer as ans where ans.question.id = q.id), " +
                                "q.persistDateTime)" +
                                "from Question q where q.user.id = :id", UserProfileQuestionDto.class)
                .setParameter("id", id)
                .getResultList();
    }
}