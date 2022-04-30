package com.javamentor.qa.platform.dao.impl.dto.pagination.questionDto.globalSearch;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("QuestionPageDtoByViews")
@RequiredArgsConstructor
public class QuestionPageDtoByViews implements PaginationDtoAble<QuestionViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getItems(Map<String, Object> param) {
        String str = ((String) param.get("parseStr")).replace("views:", "");
        String[] strings = str.split("[^?\\w+]");
        String min = strings[0];
        String max = strings[1];
        int minView = Integer.parseInt(min);
        int maxView = Integer.parseInt(max);
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        return entityManager.createQuery("SELECT DISTINCT new com.javamentor.qa.platform.models.dto.QuestionViewDto" +
                        "(question.id, " +
                        "question.title, " +
                        "author.id, " +
                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = author.id), " +
                        "author.fullName, " +
                        "author.imageLink, " +
                        "question.description, " +
                        "0L, " +
                        "(SELECT COUNT(*) FROM Answer answer WHERE answer.question.id = question.id), " +
                        "((SELECT COUNT(*) FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'UP_VOTE' AND voteOnQuestion.question.id = question.id) - " +
                        "(SELECT COUNT(*) FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'DOWN_VOTE' AND voteOnQuestion.question.id = question.id)), " +
                        "question.persistDateTime, " +
                        "question.lastUpdateDateTime) " +
                        "FROM Question question " +
                        "LEFT OUTER JOIN question.user AS author ON (question.user.id = author.id) " +
                        "LEFT OUTER JOIN question.answers AS answer ON (question.id = answer.question.id) " +
                        "LEFT JOIN QuestionViewed  q AS q  ON (q.question.id = question.id)" +
                        "WHERE (SELECT COUNT(*) FROM QuestionViewed a WHERE a.question.id = question.id AND a.user.id = author.id " +
                        "GROUP BY a.question.id HAVING :minView AND :maxView ) " +
                        "ORDER BY question.id", QuestionViewDto.class)
                .setParameter("minView", minView)
                .setParameter("maxView", maxView)
                .getResultStream()
                .skip((currentPageNumber - 1) * itemsOnPage)
                .limit(itemsOnPage)
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        String str = ((String) param.get("parseStr")).replace("views:..", "");
        int minView = Integer.parseInt(str);
        int maxView = Integer.parseInt(str);
        return Math.toIntExact((Long) entityManager.createQuery("SELECT COUNT(DISTINCT question.id) FROM Question question" +
                        "LEFT JOIN QuestionViewed  q   ON (q.question.id = question.id)" +
                        "WHERE (SELECT COUNT(*) FROM QuestionViewed a WHERE a.question.id = question.id AND a.user.id = author.id " +
                        "GROUP BY a.question.id HAVING BETWEEN :minView AND :maxView  )")
                .setParameter("minView", minView)
                .setParameter("maxView", maxView)
                .getSingleResult());

    }
}
