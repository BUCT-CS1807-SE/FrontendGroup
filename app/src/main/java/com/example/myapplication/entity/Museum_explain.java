package com.example.myapplication.entity;

public class Museum_explain {
    private Integer Id;
    private Integer Createid;
    private Integer Type;
    private Integer Museumid;
    private String Text;
    private Integer Sponsor;
    private Integer State;

    public Museum_explain(Integer id, Integer createid, Integer type, Integer museumid, String text, Integer sponsor, Integer state) {
        Id = id;
        Createid = createid;
        Type = type;
        Museumid = museumid;
        Text = text;
        Sponsor = sponsor;
        State = state;
    }
    public Museum_explain()
    {

    }


    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getCreateid() {
        return Createid;
    }

    public void setCreateid(Integer createid) {
        Createid = createid;
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        Type = type;
    }

    public Integer getMuseumid() {
        return Museumid;
    }

    public void setMuseumid(Integer museumid) {
        Museumid = museumid;
    }


    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public Integer getSponsor() {
        return Sponsor;
    }

    public void setSponsor(Integer sponsor) {
        Sponsor = sponsor;
    }

    public Integer getState() {
        return State;
    }

    public void setState(Integer state) {
        State = state;
    }
}
