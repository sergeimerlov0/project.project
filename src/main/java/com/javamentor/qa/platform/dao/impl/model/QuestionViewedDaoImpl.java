package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionViewedDaoImpl extends ReadWriteDaoImpl<QuestionViewed, Long> implements QuestionViewedDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public boolean questionViewCheckByUserIdAndQuestionId (Long questionId, Long userId) {
        List<QuestionViewed> questionViewedList = entityManager.createQuery("FROM QuestionViewed a WHERE a.question.id =:questionId and a.user.id =: userId", QuestionViewed.class)
                .setParameter("questionId", questionId)
                .setParameter("userId", userId)
                .getResultList();
        return !questionViewedList.isEmpty();
    }

    @Override
    public QuestionViewed getTotalQuestionViewByQuestionIAndUserId (Long questionId, Long userId) {
        return entityManager.createQuery("FROM QuestionViewed a WHERE a.question.id =:questionId and a.user.id =: userId", QuestionViewed.class)
                .setParameter("questionId", questionId)
                .setParameter("userId", userId)
                .getSingleResult();
    }


}
