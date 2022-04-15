package com.javamentor.qa.platform.service.search.manager.impl;


import com.javamentor.qa.platform.service.search.manager.abstrats.GlobalSearchParserString;



public class GlobalSearchParserInBody implements GlobalSearchParserString {


    @Override
    public String parseString(String parseStr) {
        if (parseStr.startsWith("body:")) {
            return "QuestionPageDtoFromBody";
        } else {
            return null;
        }
    }
}