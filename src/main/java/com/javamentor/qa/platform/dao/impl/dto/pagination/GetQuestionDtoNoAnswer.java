package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("QuestionDtoNoAnswer")
public class GetQuestionDtoNoAnswer implements PaginationDtoAble<QuestionDto> {

    @Override
    public List<QuestionDto> getItems(Map<String, Object> param) {
        return null;
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return 0;
    }
}
