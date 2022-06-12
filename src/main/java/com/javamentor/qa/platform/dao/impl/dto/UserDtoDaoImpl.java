package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDtoDaoImpl implements UserDtoDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserDto> getUserById(Long id) {
        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.UserDto " +
                        "(user.id, " +
                        "user.email, " +
                        "user.fullName, " +
                        "user.imageLink, " +
                        "user.city, " +
                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = user.id), " +
                        "user.persistDateTime) " +
                        "FROM User user " +
                        "WHERE user.id = :id " +
                        "AND user.isEnabled = true " +
                        "GROUP BY user.id",
                        UserDto.class)
                .setParameter("id", id)
                .getResultStream()
                .findAny();
    }

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

    @Override
    public List<UserProfileQuestionDto> getAllDeletedQuestionsByUserId(Long id) {
        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.UserProfileQuestionDto(" +
                                "q.id, " +
                                "q.title, " +
                                "(select (count(ans.id)) from Answer as ans where ans.question.id = q.id), " +
                                "q.persistDateTime)" +
                                "from Question q where q.user.id = :id", UserProfileQuestionDto.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<UserDto> getTop10ByAnswerPerWeek() {
        return entityManager.createQuery(
                        "SELECT DISTINCT new com.javamentor.qa.platform.models.dto.UserDto " +
                                "(user.id, " +
                                "user.email, " +
                                "user.fullName, " +
                                "user.imageLink, " +
                                "user.city, " +
                                "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = user.id), " +
                                "user.persistDateTime, " +
                                "(SELECT COUNT(a.persistDateTime) FROM Answer a WHERE a.user.id = user.id " +
                                "AND a.persistDateTime BETWEEN :week AND LOCALTIMESTAMP " +
                                "AND a.isDeleted = false) " +
                                "AS totalAnswers, " +
                                "((SELECT COUNT (*) FROM VoteAnswer va WHERE va.vote = 'UP_VOTE' AND va.answer.user.id = user.id) - " +
                                "(SELECT COUNT (*) FROM VoteAnswer va WHERE va.vote = 'DOWN_VOTE' AND va.answer.user.id = user.id)) " +
                                "AS totalVotesOnAnswers) " +
                                "FROM User user " +
                                "WHERE user.isEnabled = true " +
                                "AND (SELECT COUNT(a.persistDateTime) FROM Answer a WHERE a.user.id = user.id " +
                                "AND a.persistDateTime BETWEEN :week AND LOCALTIMESTAMP " +
                                "AND a.isDeleted = false) > 0 " +
                                "GROUP BY totalAnswers, totalVotesOnAnswers, user.id " +
                                "ORDER BY totalAnswers DESC, totalVotesOnAnswers DESC "
                        , UserDto.class)
                .setMaxResults(10)
                .setParameter("week", LocalDateTime.now().minusDays(7L))
                .getResultList();
    }

}