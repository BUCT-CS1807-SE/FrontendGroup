package com.example.myapplication.entity;

public class MuseumCollectedPost {
    Integer id;
    Integer mumid;
    Integer userid;

    public MuseumCollectedPost() {
    }

    public MuseumCollectedPost(Integer id, Integer mumid, Integer userid) {
        this.id = id;
        this.mumid = mumid;
        this.userid = userid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMumid() {
        return mumid;
    }

    public void setMumid(Integer mumid) {
        this.mumid = mumid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
