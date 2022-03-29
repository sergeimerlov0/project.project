package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class QuestionViewedDaoImpl extends ReadWriteDaoImpl<QuestionViewed, Long> implements QuestionViewedDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Cacheable(value = "checkMethod", key = "{#questionId,#email}")
    public boolean questionViewCheckByUserIdAndQuestionId(Long questionId, String email) {
        return entityManager.createQuery(
                "SELECT COUNT(a)>0 " +
                        "FROM QuestionViewed a " +
                        "WHERE a.question.id = :questionId " +
                        "AND a.user.email = :userEmail",
                        Boolean.class)
                .setParameter("questionId", questionId)
                .setParameter("userEmail", email)
                .getSingleResult();
    }

    @Override
    @CacheEvict(value = "checkMethod", key = "{#questionViewed.question.id, #questionViewed.user.email}")
    public void persist(QuestionViewed questionViewed) {
        super.persist(questionViewed);
    }
}