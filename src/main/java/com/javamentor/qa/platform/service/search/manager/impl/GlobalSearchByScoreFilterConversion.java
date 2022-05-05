package com.javamentor.qa.platform.service.search.manager.impl;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GlobalSearchByScoreFilterConversion {

    public Map<String, Object> filterConversion (Map<String, Object> map){
        String score = ((String) map.get("parseStr")).replace("score:", "").trim();
        long score1 = 0, score2 = 0;

        ArrayList<Integer> countVote = new ArrayList<>();
        Pattern integerPattern = Pattern.compile("(-?\\d+)");
        Matcher matched = integerPattern.matcher(score);
        while (matched.find()) {
            countVote.add(Integer.valueOf(matched.group()));
        }
        if (score.charAt(0) == '.') {
            score1 = Integer.MIN_VALUE;
            score2 = countVote.get(0);
        } else if (countVote.size() == 2) {
            score1 = countVote.get(0);
            score2 = countVote.get(1);
        } else if (score.charAt(score.length()-1) == '.') {
            score1 = countVote.get(0);
            score2 = Integer.MAX_VALUE;
        } else if (countVote.size() == 1) {
            score1 = countVote.get(0);
            score2 = countVote.get(0);
        }
        map.remove("parseStr");
        map.put("score1", score1);
        map.put("score2", score2);
        return map;
    }
}