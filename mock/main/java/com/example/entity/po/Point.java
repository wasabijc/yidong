package com.example.entity.po;

// 区域边界点
public class Point {
    private double lon;
    private double lat;

    public Point(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    // Getters
    public double getLon() { return lon; }
    public double getLat() { return lat; }
}
