package com.javamentor.qa.platform.service.search.manager.impl;


import com.javamentor.qa.platform.service.search.manager.abstrats.GlobalSearchParserString;

import java.util.Optional;


public class GlobalSearchParserByExactPhrase implements GlobalSearchParserString {


    @Override
    public String parseString(String parseStr) {
        if (parseStr.matches("\".*\"")) {
           return "QuestionPageDtoByExactPhrase";
       }
       return Optional.empty().toString();


    }
}




