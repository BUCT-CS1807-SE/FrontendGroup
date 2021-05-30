package com.example.myapplication.entity;

import java.io.Serializable;

public class Rating implements Serializable {
    Integer id;
    Integer usersid;
    Integer museumid;
    Integer scoreone;
    Integer scoretwo;
    Integer scorethree;

    public Rating() {
    }

    public Rating(Integer id, Integer usersid, Integer museumid, Integer scoreone, Integer scoretwo, Integer scorethree) {
        this.id = id;
        this.usersid = usersid;
        this.museumid = museumid;
        this.scoreone = scoreone;
        this.scoretwo = scoretwo;
        this.scorethree = scorethree;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsersid() {
        return usersid;
    }

    public void setUsersid(Integer usersid) {
        this.usersid = usersid;
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

    public Integer getScoretwo() {
        return scoretwo;
    }

    public void setScoretwo(Integer scoretwo) {
        this.scoretwo = scoretwo;
    }

    public Integer getScorethree() {
        return scorethree;
    }

    public void setScorethree(Integer scorethree) {
        this.scorethree = scorethree;
    }
}
