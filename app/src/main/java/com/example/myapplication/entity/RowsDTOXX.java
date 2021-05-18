package com.example.myapplication.entity;

import com.google.gson.annotations.SerializedName;

public class RowsDTOXX {
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
    @SerializedName("museumid")
    private Integer museumid;
    @SerializedName("exhibitname")
    private String exhibitname;
    @SerializedName("exhibitsummary")
    private String exhibitsummary;
    @SerializedName("collectionname")
    private String collectionname;
    @SerializedName("collectionimageurl")
    private String collectionimageurl;
    @SerializedName("collectionsummary")
    private Object collectionsummary;

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

    public Integer getMuseumid() {
        return museumid;
    }

    public void setMuseumid(Integer museumid) {
        this.museumid = museumid;
    }

    public String getExhibitname() {
        return exhibitname;
    }

    public void setExhibitname(String exhibitname) {
        this.exhibitname = exhibitname;
    }

    public String getExhibitsummary() {
        return exhibitsummary;
    }

    public void setExhibitsummary(String exhibitsummary) {
        this.exhibitsummary = exhibitsummary;
    }

    public String getCollectionname() {
        return collectionname;
    }

    public void setCollectionname(String collectionname) {
        this.collectionname = collectionname;
    }

    public String getCollectionimageurl() {
        return collectionimageurl;
    }

    public void setCollectionimageurl(String collectionimageurl) {
        this.collectionimageurl = collectionimageurl;
    }

    public Object getCollectionsummary() {
        return collectionsummary;
    }

    public void setCollectionsummary(Object collectionsummary) {
        this.collectionsummary = collectionsummary;
    }
}
