package com.example.myapplication.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class MapListResponse implements Serializable {


    @SerializedName("total")
    private Integer total;
    @SerializedName("rows")
    private List<RowsDTO> rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<RowsDTO> getRows() {
        return rows;
    }

    public void setRows(List<RowsDTO> rows) {
        this.rows = rows;
    }
}
