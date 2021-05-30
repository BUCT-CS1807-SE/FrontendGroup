package com.example.myapplication.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class Exhibition implements Serializable {

    private int id;
    @JSONField(alternateNames = "museumid")
    private int museumId;
    @JSONField(alternateNames = "exhibitname")
    private String exhibitionName;
    @JSONField(alternateNames = "exhibitsummary")
    private String exhibitionDescribe;


    public Exhibition() {
    }

    public Exhibition(int id, int museumId, String exhibitionName, String exhibitionDescribe) {
        this.id = id;
        this.museumId = museumId;
        this.exhibitionName = exhibitionName;
        this.exhibitionDescribe = exhibitionDescribe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMuseumId() {
        return museumId;
    }

    public void setMuseumId(int museumId) {
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
