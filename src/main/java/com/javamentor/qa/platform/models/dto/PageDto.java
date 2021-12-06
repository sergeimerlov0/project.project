package com.javamentor.qa.platform.models.dto;

import java.util.List;

public class PageDto<T> {

    private int currentPageNumber;
    private int totalPageCount;
    private int totalResultCount;
    private List<T> items;
    private int itemsOnPage;

    public PageDto(int currentPageNumber, int totalPageCount, int totalResultCount, List<T> items, int itemsOnPage) {
        this.currentPageNumber = currentPageNumber;
        this.totalPageCount = totalPageCount;
        this.totalResultCount = totalResultCount;
        this.items = items;
        this.itemsOnPage = itemsOnPage;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public int getTotalResultCount() {
        return totalResultCount;
    }

    public List<T> getItems() {
        return items;
    }

    public int getItemsOnPage() {
        return itemsOnPage;
    }
}
