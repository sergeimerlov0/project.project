package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserProfileQuestionDtoDao;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserProfileQuestionDtoDaoImpl implements UserProfileQuestionDtoDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserProfileQuestionDto> getUserProfileQuestionDtoAddByUserId(Long userId) {
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.UserProfileQuestionDto(" +
                        "question.id, " +
                        "question.title, " +
                        "(SELECT COUNT(*) FROM Answer answer WHERE answer.question.id = question.id AND answer.isDeleted = false), " +
                        "question.persistDateTime) " +
                        "FROM Question question " +
                        "WHERE question.user.id = :userId " +
                        "AND question.isDeleted = false",
                UserProfileQuestionDto.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}