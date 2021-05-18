package com.example.myapplication.entity;

import java.util.List;

public class NearMuseumEntity {
    private String museumName;
    private String level;
    private String openTime;
    private String ticker;
    private String ImageUrl;
    private String exhibitionName;
    private Integer museumId;
    private List<exhtestEntity> exhItem;

    public List<exhtestEntity> getExhItem() {
        return exhItem;
    }

    public void setExhItem(List<exhtestEntity> exhItem) {
        this.exhItem = exhItem;
    }

    public Integer getMuseumId() {
        return museumId;
    }

    public void setMuseumId(Integer museumId) {
        this.museumId = museumId;
    }

    public String getExhibitionName() {
        return exhibitionName;
    }

    public void setExhibitionName(String exhibitionName) {
        this.exhibitionName = exhibitionName;
    }

    public String getMuseumName() {
        return museumName;
    }

    public void setMuseumName(String museumName) {
        this.museumName = museumName;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
