package com.example.myapplication.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 新闻
 * @author 黄熠
 */
public class MuseumNew implements Serializable {

    private Integer id;//id
    private String title;//标题
    private String author;//作者
    private String time;//时间
    private String link;//原文链接
    private String content;//内容
    @JSONField(alternateNames = "mumname")
    private String museumName;//博物馆名称
    @JSONField(alternateNames = "classificationone")
    private String type1;//正面新闻还是负面新闻
    @JSONField(alternateNames = "classificationtwo")
    private String type2;//新闻类型

    public MuseumNew() {
    }

    public MuseumNew(Integer id, String title, String author, String time, String link, String content, String museumName, String type1, String type2) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.time = time;
        this.link = link;
        this.content = content;
        this.museumName = museumName;
        this.type1 = type1;
        this.type2 = type2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMuseumName() {
        return museumName;
    }

    public void setMuseumName(String museumName) {
        this.museumName = museumName;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }
}
