package com.javamentor.qa.platform.service.search.manager.impl;


import com.javamentor.qa.platform.service.search.manager.abstrats.GlobalSearchParserString;



public class GlobalSearchParserInTitle implements GlobalSearchParserString {


    @Override
    public String parseString(String parseStr) {
        if (parseStr.startsWith("title:")) {
            return "QuestionPageDtoFromTitle";
        } else {
            return null;
        }
    }
}