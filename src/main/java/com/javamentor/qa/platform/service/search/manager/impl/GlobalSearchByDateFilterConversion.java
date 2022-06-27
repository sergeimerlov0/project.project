package com.javamentor.qa.platform.service.search.manager.impl;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;


public class GlobalSearchByDateFilterConversion {

    public Map<String, Object> filterConversion (Map<String, Object> map){
        String date = ((String) map.get("parseStr")).replace("created:", "").trim();
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
        map.remove("parseStr");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        LocalDateTime data1 = LocalDateTime.parse(date1, dateFormat);
        LocalDateTime data2 = LocalDateTime.parse(date2, dateFormat);
        map.put("date1", data1);
        map.put("date2", data2);
        return map;
    }

    private static String formatAbsoluteDate1 (String date){
        String dataResult = null;
        String[] dateVar = date.split("-");
        if(dateVar.length == 1){
            dataResult = dateVar[0] + "0101" + " 00:00:00";
        } else if (dateVar.length == 2){
            dataResult = dateVar[0] + dateVar[1] + "01" + " 00:00:00";
        } else if (dateVar.length == 3){
            dataResult = dateVar[0] + dateVar[1] + dateVar[2] + " 00:00:00";
        }
        return dataResult;
    }

    private static String formatAbsoluteDate2 (String date){
        String dataResult = null;
        String[] dateVar = date.split("-");
        if(dateVar.length == 1){
            dataResult = dateVar[0] + "1231" + " 23:59:59";
        } else if (dateVar.length == 2){
            dataResult = dateVar[0] + dateVar[1] + daysInMonth.get(dateVar[1])  + " 23:59:59";
        } else if (dateVar.length == 3){
            dataResult = dateVar[0] + dateVar[1] + dateVar[2]  + " 23:59:59";
        }
        return dataResult;
    }

    private static String formatRelativeDate (String date) {
        StringBuilder dat = new StringBuilder(date);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
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
                data1.append("0101 00:00:00");
                dates[0] = data1.toString();
                data1.delete(4,17);
                data1.append("1231 23:59:59");
                dates[1] = data1.toString();
                break;
            case ('m'):
                cal.add(Calendar.MONTH, -Integer.parseInt(dat.deleteCharAt(dat.length() - 1).toString()));
                data1.append(dateFormat.format(cal.getTime()));
                data1.delete(6,8);
                data1.append("01 00:00:00");
                dates[0] = data1.toString();
                data1.delete(6,17);
                data1.append(daysInMonth.get(String.valueOf(data1.charAt(4)) + String.valueOf(data1.charAt(5))));
                data1.append(" 23:59:59");
                dates[1] = data1.toString();
                break;
            case ('d'):
                cal.add(Calendar.DATE, -Integer.parseInt(dat.deleteCharAt(dat.length() - 1).toString()));
                data1.append(dateFormat.format(cal.getTime()));
                dates[0] = data1.append(" 00:00:00").toString();
                data1.delete(8,17);
                dates[1] = data1.append(" 23:59:59").toString();
                break;
        }
        return dates;
    }
    private final static Map<String, String> daysInMonth = Map.ofEntries(
            Map.entry("01", "31"),
            Map.entry("02", "28"),
            Map.entry("03", "31"),
            Map.entry("04", "30"),
            Map.entry("05", "31"),
            Map.entry("06", "30"),
            Map.entry("07", "31"),
            Map.entry("08", "31"),
            Map.entry("09", "30"),
            Map.entry("10", "31"),
            Map.entry("11", "30"),
            Map.entry("12", "31")
    );
}