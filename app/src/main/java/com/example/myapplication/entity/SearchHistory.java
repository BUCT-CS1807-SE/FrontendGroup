package com.example.myapplication.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class SearchHistory extends LitePalSupport {
    private String searchContent;
    @Column(defaultValue = "CURRENT_TIMESTAMP")
    private Date searchTime;
    private String uuid;


    public SearchHistory(String searchContent, Date searchTime, String uuid) {
        this.searchContent = searchContent;
        this.searchTime = searchTime;
        this.uuid = uuid;
    }

    public SearchHistory(String searchContent, Date searchTime) {

        this.searchContent = searchContent;
        this.searchTime = searchTime;
    }

    public SearchHistory() {
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public Date getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(Date searchTime) {
        this.searchTime = searchTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
