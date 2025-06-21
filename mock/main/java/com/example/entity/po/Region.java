package com.example.entity.po;

import java.util.ArrayList;
import java.util.List;

// 区域实体类
public class Region {
    private long regionId;
    private String name;
    private List<Long> cellIds;
    private List<Point> polygon;

    public Region(long regionId, String name, List<Long> cellIds) {
        this.regionId = regionId;
        this.name = name;
        this.cellIds = cellIds;
        this.polygon = new ArrayList<>();
    }

    // Getters and Setters
    public long getRegionId() { return regionId; }
    public String getName() { return name; }
    public List<Long> getCellIds() { return cellIds; }
    public List<Point> getPolygon() { return polygon; }
    public void setPolygon(List<Point> polygon) { this.polygon = polygon; }
}
