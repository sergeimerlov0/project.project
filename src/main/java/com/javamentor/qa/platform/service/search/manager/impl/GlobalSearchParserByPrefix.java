package com.javamentor.qa.platform.service.search.manager.impl;

import com.javamentor.qa.platform.service.search.manager.abstrats.GlobalSearchParserString;

import java.util.Optional;

public class GlobalSearchParserByPrefix implements GlobalSearchParserString {
    @Override
    public String parseString(String parseStr) {
        if (!parseStr.startsWith("*") && parseStr.endsWith("*")) {
            return "QuestionPageDtoByPrefix";
        }
        return Optional.empty().toString();
    }
}
