package com.javamentor.qa.platform.service.search.manager.impl;


import com.javamentor.qa.platform.service.search.manager.abstrats.GlobalSearchParserString;

import java.util.Optional;


public class GlobalSearchParserByTags implements GlobalSearchParserString {


    @Override
    public String parseString(String parseStr) {
        if (parseStr.matches("\\[.*\\]")) {
           return ("QuestionPageDtoByTags");
       }
       return Optional.empty().toString();


    }
}




