package com.example.myapplication.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class Exhibition implements Serializable {

    private Integer id;
    private Integer museumId;
    @JSONField(alternateNames = "exhibitname")
    private String exhibitionName;
    @JSONField(alternateNames = "exhibitsummary")
    private String exhibitionDescribe;


    public Exhibition() {
    }

    public Exhibition(Integer id, Integer museumId, String exhibitionName, String exhibitionDescribe) {
        this.id = id;
        this.museumId = museumId;
        this.exhibitionName = exhibitionName;
        this.exhibitionDescribe = exhibitionDescribe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getExhibitionDescribe() {
        return exhibitionDescribe;
    }

    public void setExhibitionDescribe(String exhibitionDescribe) {
        this.exhibitionDescribe = exhibitionDescribe;
    }
}
