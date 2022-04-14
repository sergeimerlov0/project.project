package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.BookmarksDtoDao;
import com.javamentor.qa.platform.models.dto.BookmarksDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BookmarksDtoDaoImpl implements BookmarksDtoDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BookmarksDto> getBookmarksDtoByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.BookmarksDto" +
                                "(bm.id," +
                                "bm.question.id," +
                                "bm.persistDateTime)" +
                                "FROM BookMarks bm " +
                                "WHERE bm.user.id = :userId ",
                        BookmarksDto.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}