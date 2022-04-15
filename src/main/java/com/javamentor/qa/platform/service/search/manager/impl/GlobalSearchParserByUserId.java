package com.javamentor.qa.platform.service.search.manager.impl;

import com.javamentor.qa.platform.service.search.manager.abstrats.GlobalSearchParserString;



public class GlobalSearchParserByUserId implements GlobalSearchParserString {

    @Override
    public String parseString(String parseStr) {
        if (parseStr.startsWith("user:id_")) {
            return "QuestionPageDtoByUserId";
        } else {
            return null;
        }
    }
}


