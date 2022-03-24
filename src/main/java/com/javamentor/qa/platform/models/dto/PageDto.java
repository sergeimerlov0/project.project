package com.javamentor.qa.platform.models.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {
    private int currentPageNumber;
    private int totalPageCount;
    private int totalResultCount;
    private List<T> items;
    private int itemsOnPage;
}