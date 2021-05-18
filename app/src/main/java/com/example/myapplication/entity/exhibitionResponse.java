package com.example.myapplication.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class exhibitionResponse {

    @SerializedName("total")
    private Integer total;
    @SerializedName("rows")
    private List<RowsDTOX> rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<RowsDTOX> getRows() {
        return rows;
    }

    public void setRows(List<RowsDTOX> rows) {
        this.rows = rows;
    }
}
