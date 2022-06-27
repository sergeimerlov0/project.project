package com.javamentor.qa.platform.service.search.manager.impl;


import com.javamentor.qa.platform.service.search.manager.abstrats.GlobalSearchManager;


import java.util.ArrayList;
import java.util.List;


public class GlobalSearchManagerImpl implements GlobalSearchManager {


    @Override
    public List<String> filter(String parseStr) {
       List<String> parserResult = new ArrayList<>();
       parserResult.add(new GlobalSearchParserInTitle().parseString(parseStr));
       parserResult.add(new GlobalSearchParserInBody().parseString(parseStr));
       parserResult.add(new GlobalSearchParserByUserId().parseString(parseStr));
       parserResult.add(new GlobalSearchParserByExactPhrase().parseString(parseStr));
       parserResult.add(new GlobalSearchParserByPrefix().parseString(parseStr));
       parserResult.add(new GlobalSearchParserByPostfix().parseString(parseStr));
       parserResult.add(new GlobalSearchParserByAnswers().parseString(parseStr));
       parserResult.add(new GlobalSearchParserByTags().parseString(parseStr));
       parserResult.add(new GlobalSearchParserByAuthUser().parseString(parseStr));
       parserResult.add(new GlobalSearchParserByView().parseString(parseStr));
       parserResult.add(new GlobalSearchParserByDate().parseString(parseStr));
       parserResult.add(new GlobalSearchParserByScore().parseString(parseStr));
       //в parserResult добавляем результаты Parser'ов по разным условиям
        return parserResult;

    }
}
