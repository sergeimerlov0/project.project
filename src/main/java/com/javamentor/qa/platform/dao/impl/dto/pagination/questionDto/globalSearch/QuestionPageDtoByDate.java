package com.javamentor.qa.platform.dao.impl.dto.pagination.questionDto.globalSearch;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("QuestionPageDtoByData")
@RequiredArgsConstructor
public class QuestionPageDtoByDate implements PaginationDtoAble<QuestionViewDto> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getItems(Map<String, Object> param) {
        String date = ((String) param.get("parseStr")).replace("created:", "").trim();
        String date1 = null, date2 = null;

        String[] dates = date.split("\\.+");
        if (date.indexOf('y') > -1 || date.indexOf('m') > -1 || date.indexOf('d') > -1){
            if(date.indexOf('.') > -1){
                if (dates.length == 1) {
                    date1 = formatRelativeDate(dates[0]);
                    date2 = formatRelativeDate("0d");
                } else if (dates.length == 2) {
                    date1 = formatRelativeDate(dates[0]);
                    date2 = formatRelativeDate(dates[1]);
                }
            } else {
                String[] dates2 = formatRelativeDate1(dates[0]);
                date1 = dates2[0];
                date2 = dates2[1];
            }
        } else {
            if (dates.length == 1) {
                date1 = formatAbsoluteDate1(dates[0]);
                date2 = formatAbsoluteDate2(dates[0]);

            } else if (dates.length == 2) {
                date1 = formatAbsoluteDate1(dates[0]);
                date2 = formatAbsoluteDate2(dates[1]);
            }
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
        if (date.indexOf('y') > -1 || date.indexOf('m') > -1 || date.indexOf('d') > -1){
            if(date.indexOf('.') > -1){
                if (dates.length == 1) {
                    date1 = formatRelativeDate(dates[0]);
                    date2 = formatRelativeDate("0d");
                } else if (dates.length == 2) {
                    date1 = formatRelativeDate(dates[0]);
                    date2 = formatRelativeDate(dates[1]);
                }
            } else {
                String[] dates2 = formatRelativeDate1(dates[0]);
                date1 = dates2[0];
                date2 = dates2[1];
            }
        } else {
            if (dates.length == 1) {
                date1 = formatAbsoluteDate1(dates[0]);
                date2 = formatAbsoluteDate2(dates[0]);

            } else if (dates.length == 2) {
                date1 = formatAbsoluteDate1(dates[0]);
                date2 = formatAbsoluteDate2(dates[1]);
            }
        }

        return Math.toIntExact((Long) entityManager.createQuery("SELECT COUNT(DISTINCT question.id) FROM Question question LEFT JOIN question.user " +
                        "WHERE question.persistDateTime >= :date1 AND question.persistDateTime <= :date2")
                .setParameter("date1", date1)
                .setParameter("date2", date2)
                .getSingleResult());

    }

    private static String formatAbsoluteDate1 (String date){
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

    private static String formatAbsoluteDate2 (String date){
        String dataResult = null;
        HashMap <String, String> daysInMonth = new HashMap<>();
        daysInMonth.put("01", "31");
        daysInMonth.put("02", "28");
        daysInMonth.put("03", "31");
        daysInMonth.put("04", "30");
        daysInMonth.put("05", "31");
        daysInMonth.put("06", "30");
        daysInMonth.put("07", "31");
        daysInMonth.put("08", "31");
        daysInMonth.put("09", "30");
        daysInMonth.put("10", "31");
        daysInMonth.put("11", "30");
        daysInMonth.put("12", "31");
        String[] dateVar = date.split("-");
        if(dateVar.length == 1){
            dataResult = dateVar[0] + "1231";
        } else if (dateVar.length == 2){
            dataResult = dateVar[0] + dateVar[1] + daysInMonth.get(dateVar[1]);
        } else if (dateVar.length == 3){
            dataResult = dateVar[0] + dateVar[1] + dateVar[2];
        }
        return dataResult;
    }

    private static String formatRelativeDate (String date) {
        StringBuilder dat = new StringBuilder(date);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        switch (date.charAt(date.length() - 1)) {
            case ('y'):
                cal.add(Calendar.YEAR, -Integer.parseInt(dat.deleteCharAt(dat.length() - 1).toString()));
                break;
            case ('m'):
                cal.add(Calendar.MONTH, -Integer.parseInt(dat.deleteCharAt(dat.length() - 1).toString()));
                break;
            case ('d'):
                cal.add(Calendar.DATE, -Integer.parseInt(dat.deleteCharAt(dat.length() - 1).toString()));
                break;
        }
        return dateFormat.format(cal.getTime());
    }

    private static String[] formatRelativeDate1 (String date) {
        HashMap <String, String> daysInMonth = new HashMap<>();
        daysInMonth.put("01", "31");
        daysInMonth.put("02", "28");
        daysInMonth.put("03", "31");
        daysInMonth.put("04", "30");
        daysInMonth.put("05", "31");
        daysInMonth.put("06", "30");
        daysInMonth.put("07", "31");
        daysInMonth.put("08", "31");
        daysInMonth.put("09", "30");
        daysInMonth.put("10", "31");
        daysInMonth.put("11", "30");
        daysInMonth.put("12", "31");
        StringBuilder dat = new StringBuilder(date);
        StringBuilder data1 = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        String [] dates = new String[2];
        switch (date.charAt(date.length() - 1)) {
            case ('y'):
                cal.add(Calendar.YEAR, -Integer.parseInt(dat.deleteCharAt(dat.length() - 1).toString()));
                data1.append(dateFormat.format(cal.getTime()));
                data1.delete(4,8);
                data1.append("0101");
                dates[0] = data1.toString();
                data1.delete(4,8);
                data1.append("1231");
                dates[1] = data1.toString();
                break;
            case ('m'):
                cal.add(Calendar.MONTH, -Integer.parseInt(dat.deleteCharAt(dat.length() - 1).toString()));
                data1.append(dateFormat.format(cal.getTime()));
                data1.delete(6,8);
                data1.append("01");
                dates[0] = data1.toString();
                data1.delete(6,8);
                data1.append(daysInMonth.get(data1.charAt(4) + data1.charAt(5)));
                dates[1] = data1.toString();
                break;
            case ('d'):
                cal.add(Calendar.DATE, -Integer.parseInt(dat.deleteCharAt(dat.length() - 1).toString()));
                data1.append(dateFormat.format(cal.getTime()));
                dates[0] = data1.toString();
                dates[1] = data1.toString();
                break;
        }
        return dates;
    }
}