package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.BookmarksDao;
import com.javamentor.qa.platform.models.entity.BookMarks;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BookmarksDaoImpl extends ReadWriteDaoImpl<BookMarks, Long> implements BookmarksDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Long> bookmarkedQuestionByUserId(Long userId) {
        return entityManager
                .createQuery("select t.question.id from BookMarks t where t.user.id=:id", Long.class)
                .setParameter("id", userId).getResultList();
    }

    @Override
    public boolean bookmarkExistsByQuestionIdByUserId(Long userId, Long questionId) {
        long count = entityManager
                .createQuery("select count(t) from BookMarks t where t.user.id=:userId and t.question.id=:questionId", Long.class)
                .setParameter("userId", userId)
                .setParameter("questionId", questionId)
                .getSingleResult();
        return count > 0;
    }
}
