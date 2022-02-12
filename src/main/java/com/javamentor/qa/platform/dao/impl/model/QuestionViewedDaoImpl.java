package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class QuestionViewedDaoImpl extends ReadWriteDaoImpl<QuestionViewed, Long> implements QuestionViewedDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Cacheable(value = "checkMetod")
    public boolean questionViewCheckByUserIdAndQuestionId (Long questionId, String email) {
        String cacheKey = email + questionId;
        return entityManager.createQuery("SELECT COUNT(a)>0 FROM QuestionViewed a WHERE a.question.id =:questionId and a.user.email =: useremail", Boolean.class)
                .setParameter("questionId", questionId)
                .setParameter("useremail", email)
                .getSingleResult();
    }

    @Override
    @CacheEvict(value = "checkMetod", allEntries = true)
    public void persist (QuestionViewed questionViewed) {
        super.persist(questionViewed);
    }

}
