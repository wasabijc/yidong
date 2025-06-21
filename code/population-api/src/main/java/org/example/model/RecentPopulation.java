package com.example.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentPopulation {
    private String regionId;
    private long currentCount;
    private double trendPercentage;
    private LocalDateTime lastUpdated;
}