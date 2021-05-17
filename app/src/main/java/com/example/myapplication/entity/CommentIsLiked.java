package com.example.myapplication.entity;

public class CommentIsLiked {
    Integer commentid;
    Integer userid;
    Integer islike;

    public CommentIsLiked() {
    }

    public CommentIsLiked(Integer commentid, Integer userid, Integer islike) {
        this.commentid = commentid;
        this.userid = userid;
        this.islike = islike;
    }

    public Integer getCommentid() {
        return commentid;
    }

    public void setCommentid(Integer commentid) {
        this.commentid = commentid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getIslike() {
        return islike;
    }

    public void setIslike(Integer islike) {
        this.islike = islike;
    }
}
