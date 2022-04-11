package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserProfileDeletedQuestionDtoDao;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserProfileDeletedQuestionDtoDaoImpl implements UserProfileDeletedQuestionDtoDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserProfileQuestionDto> getUserProfileDeletedQuestionDtoByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.UserProfileQuestionDto(" +
                                "question.id, " +
                                "question.title, " +
                                "(SELECT COUNT(*) FROM Answer answer WHERE answer.question.id = question.id AND answer.isDeleted = true), " +
                                "question.persistDateTime) " +
                                "FROM Question question " +
                                "WHERE question.user.id = :userId " +
                                "AND question.isDeleted = true",
                        UserProfileQuestionDto.class)
                .setParameter("userId", userId)
                .getResultList();
    }

}
