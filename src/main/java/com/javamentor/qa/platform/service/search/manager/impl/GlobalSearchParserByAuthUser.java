package com.javamentor.qa.platform.service.search.manager.impl;


import com.javamentor.qa.platform.service.search.manager.abstrats.GlobalSearchParserString;

import java.util.Optional;


public class GlobalSearchParserByAuthUser implements GlobalSearchParserString {


    @Override
    public String parseString(String parseStr) {
        if (parseStr.startsWith("user:me")) {
           return "QuestionPageDtoByAuthPrincipal";
        }
        return Optional.empty().toString();
    }
}