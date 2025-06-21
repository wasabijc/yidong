package com.example.entity.po;

import java.time.Instant;

// 区域快照类
public class RegionSnapshot {
    private long regionId;
    private Instant timestamp;
    private int totalCount;
    private int maleCount;
    private int femaleCount;
    private int age10_20;
    private int age20_40;
    private int age40Plus;

    public RegionSnapshot(long regionId, Instant timestamp) {
        this.regionId = regionId;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public long getRegionId() { return regionId; }
    public Instant getTimestamp() { return timestamp; }
    public int getTotalCount() { return totalCount; }
    public int getMaleCount() { return maleCount; }
    public int getFemaleCount() { return femaleCount; }
    public int getAge10_20() { return age10_20; }
    public int getAge20_40() { return age20_40; }
    public int getAge40Plus() { return age40Plus; }

    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    public void setMaleCount(int maleCount) { this.maleCount = maleCount; }
    public void setFemaleCount(int femaleCount) { this.femaleCount = femaleCount; }
    public void setAge10_20(int age10_20) { this.age10_20 = age10_20; }
    public void setAge20_40(int age20_40) { this.age20_40 = age20_40; }
    public void setAge40Plus(int age40Plus) { this.age40Plus = age40Plus; }

    // Increment methods
    public void incrementMale() { maleCount++; }
    public void incrementFemale() { femaleCount++; }
    public void incrementAge10_20() { age10_20++; }
    public void incrementAge20_40() { age20_40++; }
    public void incrementAge40Plus() { age40Plus++; }
}
