package com.example.myapplication.entity;

import com.google.gson.annotations.SerializedName;


public class RowsDTO {
    @SerializedName("searchValue")
    private Object searchValue;
    @SerializedName("createBy")
    private Object createBy;
    @SerializedName("createTime")
    private Object createTime;
    @SerializedName("updateBy")
    private Object updateBy;
    @SerializedName("updateTime")
    private Object updateTime;
    @SerializedName("remark")
    private Object remark;
    @SerializedName("params")
    private ParamsDTO params;
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("address")
    private String address;
    @SerializedName("ticketprice")
    private Integer ticketprice;
    @SerializedName("openinghours")
    private String openinghours;
    @SerializedName("suggestedtraveltime")
    private String suggestedtraveltime;
    @SerializedName("museumlevel")
    private String museumlevel;
    @SerializedName("units")
    private String units;
    @SerializedName("attractionlevel")
    private String attractionlevel;
    @SerializedName("number")
    private String number;
    @SerializedName("introduction")
    private String introduction;
    @SerializedName("scenery")
    private Object scenery;
    @SerializedName("howtogo")
    private String howtogo;
    @SerializedName("scenicspotsaround")
    private String scenicspotsaround;
    @SerializedName("cover")
    private String cover;
    @SerializedName("note")
    private Object note;
    @SerializedName("longitude")
    private Integer longitude;
    @SerializedName("latitude")
    private Integer latitude;

    public Object getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(Object searchValue) {
        this.searchValue = searchValue;
    }

    public Object getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Object createBy) {
        this.createBy = createBy;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Object updateBy) {
        this.updateBy = updateBy;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public ParamsDTO getParams() {
        return params;
    }

    public void setParams(ParamsDTO params) {
        this.params = params;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getTicketprice() {
        return ticketprice;
    }

    public void setTicketprice(Integer ticketprice) {
        this.ticketprice = ticketprice;
    }

    public String getOpeninghours() {
        return openinghours;
    }

    public void setOpeninghours(String openinghours) {
        this.openinghours = openinghours;
    }

    public String getSuggestedtraveltime() {
        return suggestedtraveltime;
    }

    public void setSuggestedtraveltime(String suggestedtraveltime) {
        this.suggestedtraveltime = suggestedtraveltime;
    }

    public String getMuseumlevel() {
        return museumlevel;
    }

    public void setMuseumlevel(String museumlevel) {
        this.museumlevel = museumlevel;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getAttractionlevel() {
        return attractionlevel;
    }

    public void setAttractionlevel(String attractionlevel) {
        this.attractionlevel = attractionlevel;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Object getScenery() {
        return scenery;
    }

    public void setScenery(Object scenery) {
        this.scenery = scenery;
    }

    public String getHowtogo() {
        return howtogo;
    }

    public void setHowtogo(String howtogo) {
        this.howtogo = howtogo;
    }

    public String getScenicspotsaround() {
        return scenicspotsaround;
    }

    public void setScenicspotsaround(String scenicspotsaround) {
        this.scenicspotsaround = scenicspotsaround;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Object getNote() {
        return note;
    }

    public void setNote(Object note) {
        this.note = note;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }
}
