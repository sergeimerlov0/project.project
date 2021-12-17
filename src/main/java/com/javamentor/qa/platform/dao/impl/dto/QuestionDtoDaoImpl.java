package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuestionDtoDaoImpl implements QuestionDtoDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<QuestionDto> getQuestionDtoByQuestionId(Long id) {

        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto." +
                "QuestionDto(question.id, question.title, author.id, 0L, author.fullName, author.imageLink, " +
                "question.description, 0L, 0L, 0L, question.persistDateTime, question.lastUpdateDateTime) " +
                "from Question question " +
                "join question.user as author " +
                "join question.answers as answer " +
                "where question.id = :id", QuestionDto.class).setParameter("id", id).getResultStream().findAny();
    }

    //JPa конструктор, result transformer.
}
