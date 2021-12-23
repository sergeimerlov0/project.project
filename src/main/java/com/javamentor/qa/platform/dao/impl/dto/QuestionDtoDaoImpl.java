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

        //@Todo в дальнейшем реализовать подсчёт просмотров (пока стоит 0)

        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto." +
                "QuestionDto(question.id, question.title, author.id, " +
                "(SELECT sum (reputation.count) from Reputation reputation where reputation.author.id = :id), " +
                "author.fullName, author.imageLink, " +
                "question.description, 0L, " +
                "(SELECT count (*) from Answer answer where answer.question.id = :id), " +
                "((SELECT count (*) from VoteQuestion voteOnQuestion " +
                "where voteOnQuestion.vote = 'UP_VOTE' and voteOnQuestion.question.id = :id) - " +
                "(SELECT count (*) from VoteQuestion voteOnQuestion " +
                "where voteOnQuestion.vote = 'DOWN_VOTE' and voteOnQuestion.question.id = :id)), " +
                "question.persistDateTime, question.lastUpdateDateTime) " +
                "from Question question " +
                "join question.user as author " +
                "join question.answers as answer " +
                "where question.id = :id", QuestionDto.class).setParameter("id", id).getResultStream().findAny();
    }
}
