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

@Repository("QuestionPageDtoByData")
@RequiredArgsConstructor
public class QuestionPageDtoByData implements PaginationDtoAble<QuestionViewDto> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getItems(Map<String, Object> param) {
        String date = ((String) param.get("parseStr")).replace("created:", "").trim();
        String date1 = null, date2 = null;

        String[] dates = date.split("\\.+");
        if (dates.length == 1){
            date1 = formatDate1(dates[0]);
            date2 = formatDate2(dates[0]);

        } else if (dates.length == 2){
            date1 = formatDate1(dates[0]);
            date2 = formatDate2(dates[1]);
        }

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
                        "LEFT JOIN question.user WHERE question.persistDateTime >= :date1 AND question.persistDateTime <= :date2 ORDER BY question.id", QuestionViewDto.class)
                .setParameter("date1", date1)
                .setParameter("date2", date2)
                .getResultStream()
                .skip((long) (currentPageNumber - 1) * itemsOnPage)
                .limit(itemsOnPage)
                .collect(Collectors.toList());

    }


    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        String date = ((String) param.get("parseStr")).replace("created:", "").trim();
        String date1 = null, date2 = null;

        String[] dates = date.split("\\.+");
        if (dates.length == 1){
            date1 = formatDate1(dates[0]);
            date2 = formatDate2(dates[0]);

        } else if (dates.length == 2){
            date1 = formatDate1(dates[0]);
            date2 = formatDate2(dates[1]);
        }

        return Math.toIntExact((Long) entityManager.createQuery("SELECT COUNT(DISTINCT question.id) FROM Question question LEFT JOIN question.user " +
                        "WHERE question.persistDateTime >= :date1 AND question.persistDateTime <= :date2")
                .setParameter("date1", date1)
                .setParameter("date2", date2)
                .getSingleResult());

    }

    private static String formatDate1 (String date){
        String dataResult = null;
        String[] dateVar = date.split("-");
        if(dateVar.length == 1){
            dataResult = dateVar[0] + "0101";
        } else if (dateVar.length == 2){
            dataResult = dateVar[0] + dateVar[1] + "01";
        } else if (dateVar.length == 3){
            dataResult = dateVar[0] + dateVar[1] + dateVar[2];
        }
        return dataResult;
    }

    private static String formatDate2 (String date){
        String dataResult = null;
        String[] dateVar = date.split("-");
        if(dateVar.length == 1){
            dataResult = dateVar[0] + "1231";
        } else if (dateVar.length == 2){
            dataResult = dateVar[0] + dateVar[1] + "31";
        } else if (dateVar.length == 3){
            dataResult = dateVar[0] + dateVar[1] + dateVar[2];
        }
        return dataResult;
    }

}