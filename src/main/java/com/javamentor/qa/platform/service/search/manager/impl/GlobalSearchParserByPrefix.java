package com.javamentor.qa.platform.service.search.manager.impl;

import com.javamentor.qa.platform.service.search.manager.abstrats.GlobalSearchParserString;

public class GlobalSearchParserByPrefix implements GlobalSearchParserString {
    @Override
    public String parseString(String parseStr) {
        StringBuilder result = new StringBuilder();
        String[] massive = parseStr.split(" ");
        for(String s : massive) {
            if (!s.startsWith("-")) {
                result.append(s).append(" ");
            }
        }

        return result.toString();
    }
}
