package com.example.myapplication.entity;

public class Rating {
    Integer id;
    Integer userid;
    Integer museumid;
    Integer scoreone;
    Integer scoretow;
    Integer scorethree;

    public Rating() {
    }

    public Rating(Integer id, Integer userid, Integer museumid, Integer scoreone, Integer scoretow, Integer scoreThree) {
        this.id = id;
        this.userid = userid;
        this.museumid = museumid;
        this.scoreone = scoreone;
        this.scoretow = scoretow;
        this.scorethree = scoreThree;
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

    public Integer getMuseumid() {
        return museumid;
    }

    public void setMuseumid(Integer museumid) {
        this.museumid = museumid;
    }

    public Integer getScoreone() {
        return scoreone;
    }

    public void setScoreone(Integer scoreone) {
        this.scoreone = scoreone;
    }

    public Integer getScoretow() {
        return scoretow;
    }

    public void setScoretow(Integer scoretow) {
        this.scoretow = scoretow;
    }

    public Integer getScoreThree() {
        return scorethree;
    }

    public void setScoreThree(Integer scoreThree) {
        this.scorethree = scoreThree;
    }
}
