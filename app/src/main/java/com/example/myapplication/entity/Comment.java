package com.example.myapplication.entity;

import java.io.Serializable;

public class Comment implements Serializable {

    private Integer id;
    private Integer userid;
    private Integer mumid;
    private String username;
    private String time;
    private String content;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userid=" + userid +
                ", mumid=" + mumid +
                ", username='" + username + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public Comment() {
    }

    public Comment(Integer id, Integer userid, Integer mumid, String username, String time, String content) {
        this.id = id;
        this.userid = userid;
        this.mumid = mumid;
        this.username = username;
        this.time = time;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getMumid() {
        return mumid;
    }

    public void setMumid(Integer mumid) {
        this.mumid = mumid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
