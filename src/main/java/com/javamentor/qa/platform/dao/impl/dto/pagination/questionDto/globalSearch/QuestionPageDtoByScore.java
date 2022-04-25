package com.javamentor.qa.platform.dao.impl.dto.pagination.questionDto.globalSearch;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository("QuestionPageDtoByScore")
@RequiredArgsConstructor
public class QuestionPageDtoByScore implements PaginationDtoAble<QuestionViewDto> {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<QuestionViewDto> getItems(Map<String, Object> param) {
        String score = ((String) param.get("parseStr")).replace("score:", "").trim();
        int voteQuestion;
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");

        ArrayList<Integer> countVote = new ArrayList<>();
        Pattern integerPattern = Pattern.compile("(-?\\d+)");
        Matcher matched = integerPattern.matcher(score);
        while (matched.find()) {
            countVote.add(Integer.valueOf(matched.group()));
        }

        if (score.charAt(0) == '.') {
            voteQuestion = countVote.get(0);

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
                            "LEFT JOIN question.user WHERE question.voteQuestions.size <= :voteQuestion ORDER BY question.id", QuestionViewDto.class)
                    .setParameter("voteQuestion", voteQuestion)
                    .getResultStream()
                    .skip((long)(currentPageNumber - 1) * itemsOnPage)
                    .limit(itemsOnPage)
                    .collect(Collectors.toList());
        } else if (countVote.size() == 2) {
            voteQuestion = countVote.get(0);
            int voteQuestion1 = countVote.get(1);

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
                            "LEFT JOIN question.user WHERE (question.voteQuestions.size >= :voteQuestion AND question.voteQuestions.size <= :voteQuestion1) " +
                            "ORDER BY question.id", QuestionViewDto.class)
                    .setParameter("voteQuestion", voteQuestion)
                    .setParameter("voteQuestion1", voteQuestion1)
                    .getResultStream()
                    .skip((long)(currentPageNumber - 1) * itemsOnPage)
                    .limit(itemsOnPage)
                    .collect(Collectors.toList());

        } else if (countVote.size() == 1) {
            voteQuestion = countVote.get(0);

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
                            "LEFT JOIN question.user WHERE question.voteQuestions.size >= :voteQuestion ORDER BY question.id", QuestionViewDto.class)
                    .setParameter("voteQuestion", voteQuestion)
                    .getResultStream()
                    .skip((long)(currentPageNumber - 1) * itemsOnPage)
                    .limit(itemsOnPage)
                    .collect(Collectors.toList());
        }
        return null;
    }


    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        String score = ((String) param.get("parseStr")).replace("score:", "").trim();
        int voteQuestion;
        ArrayList<Integer> countVote = new ArrayList<>();
        Pattern integerPattern = Pattern.compile("(-?\\d+)");
        Matcher matched = integerPattern.matcher(score);
        while (matched.find()) {
            countVote.add(Integer.valueOf(matched.group()));
        }

        if (score.charAt(0) == '.') {
            voteQuestion = countVote.get(0);

            return Math.toIntExact((Long) entityManager.createQuery("SELECT COUNT(DISTINCT question.id) FROM Question question LEFT JOIN question.user " +
                            "WHERE question.voteQuestions.size <= :voteQuestion")
                    .setParameter("voteQuestion", voteQuestion)
                    .getSingleResult());
        } else if (countVote.size() == 2) {
            voteQuestion = countVote.get(0);
            int voteQuestion1 = countVote.get(1);
            return Math.toIntExact((Long) entityManager.createQuery("SELECT COUNT(DISTINCT question.id) FROM Question question LEFT JOIN question.user " +
                            "WHERE question.voteQuestions.size >= :voteQuestion AND question.voteQuestions.size <= :voteQuestion1")
                    .setParameter("voteQuestion", voteQuestion)
                    .setParameter("voteQuestion1", voteQuestion1)
                    .getSingleResult());
        } else if (countVote.size() == 1) {
            voteQuestion = countVote.get(0);
            return Math.toIntExact((Long) entityManager.createQuery("SELECT COUNT(DISTINCT question.id) FROM Question question LEFT JOIN question.user " +
                            "WHERE question.voteQuestions.size >= :voteQuestion")
                    .setParameter("voteQuestion", voteQuestion)
                    .getSingleResult());
        }
        return 0;
    }
}