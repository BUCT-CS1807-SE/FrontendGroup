package com.example.myapplication.entity;

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
    private String museumName;//博物馆名称

    public MuseumNew(Integer id, String title, String author, String time, String link, String content, String museumName) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.time = time;
        this.link = link;
        this.content = content;
        this.museumName = museumName;
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
}
