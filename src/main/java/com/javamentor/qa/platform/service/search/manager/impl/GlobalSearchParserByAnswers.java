package com.javamentor.qa.platform.service.search.manager.impl;

import com.javamentor.qa.platform.service.search.manager.abstrats.GlobalSearchParserString;

import java.util.Optional;

public class GlobalSearchParserByAnswers implements GlobalSearchParserString {
    @Override
    public String parseString(String parseStr) {
        if (parseStr.startsWith("answers:..")) {
            return "QuestionPageDtoByAnswers";
        }
        return Optional.empty().toString();
    }
}
