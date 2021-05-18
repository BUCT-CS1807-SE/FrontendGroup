package com.example.myapplication.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class exhibitionItem {

    @SerializedName("total")
    private Integer total;
    @SerializedName("rows")
    private List<RowsDTOXX> rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<RowsDTOXX> getRows() {
        return rows;
    }

    public void setRows(List<RowsDTOXX> rows) {
        this.rows = rows;
    }
}
