package com.example.myapplication.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class Item implements Serializable {

    private Integer id;
    private Integer museumId;
    @JSONField(alternateNames = "exhibitname")
    private String showName;
    @JSONField(alternateNames = "exhibitsummary")
    private String showIntro;
    @JSONField(alternateNames = "collectionsummary")
    private String itemIntro;
    @JSONField(alternateNames = "collectionname")
    private String itemName;
    @JSONField(alternateNames = "collectionimageurl")
    private String imageAddress;

    public Item() {
    }

    public Item(Integer id, Integer museumId, String showName, String showIntro, String itemIntro, String itemName, String imageAddress) {
        this.id = id;
        this.museumId = museumId;
        this.showName = showName;
        this.showIntro = showIntro;
        this.itemIntro = itemIntro;
        this.itemName = itemName;
        this.imageAddress = imageAddress;
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

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowIntro() {
        return showIntro;
    }

    public void setShowIntro(String showIntro) {
        this.showIntro = showIntro;
    }

    public String getItemIntro() {
        return itemIntro;
    }

    public void setItemIntro(String itemIntro) {
        this.itemIntro = itemIntro;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }
}
