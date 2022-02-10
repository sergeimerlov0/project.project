package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class QuestionViewedDaoImpl extends ReadWriteDaoImpl<QuestionViewed, Long> implements QuestionViewedDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
   //@Cacheable("questionViewCheckByUserIdAndQuestionId")
    public boolean questionViewCheckByUserIdAndQuestionId (Long questionId, Long userId) {
        return entityManager.createQuery("SELECT COUNT(a)>0 FROM QuestionViewed a WHERE a.question.id =:questionId and a.user.id =: userId", Boolean.class)
                         .setParameter("questionId", questionId)
                         .setParameter("userId", userId)
                         .getSingleResult();
    }

}
