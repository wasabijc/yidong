package com.example.entity.po;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// 基站实体类
public class Cell {
    private long cellId;
    private double lat;
    private double lon;

    public Cell(long cellId, double lat, double lon) {
        this.cellId = cellId;
        this.lat = lat;
        this.lon = lon;
    }

    // Getters
    public long getCellId() { return cellId; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
}

