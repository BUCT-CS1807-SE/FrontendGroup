package com.example.myapplication.entity;

public class CommentLikedSingleInfo {
    int id;
    int commentid;
    int userid;
    int islike;

    public CommentLikedSingleInfo() {
    }

    public CommentLikedSingleInfo(int id, int commentid, int userid, int islike) {
        this.id = id;
        this.commentid = commentid;
        this.userid = userid;
        this.islike = islike;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }
}
