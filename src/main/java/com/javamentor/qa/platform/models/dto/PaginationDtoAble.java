package com.javamentor.qa.platform.models.dto;

import java.util.List;
import java.util.Map;

public interface PaginationDtoAble<T> {

    List<T> getItems(Map<String, Object> param);
    int getTotalResultCount(Map<String, Object> param);
}
