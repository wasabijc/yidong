package com.example.api.controller;

import com.example.api.model.PopulationResponse;
import com.example.api.model.RecentPopulation;
import com.example.api.service.PopulationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
public class AnalyticsController {

    private final PopulationService populationService;

    public AnalyticsController(PopulationService populationService) {
        this.populationService = populationService;
    }

    /**
     * 查询指定区域和时间段的人口统计信息
     */
    @GetMapping("/regions/{regionId}/population")
    public ResponseEntity<PopulationResponse> getPopulation(
            @PathVariable String regionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        PopulationResponse response = populationService.queryPopulation(regionId, startTime, endTime);
        return ResponseEntity.ok(response);
    }

    /**
     * 查询最近5分钟的人群数量（通过Dify Agent）
     */
    @GetMapping("/regions/{regionId}/population/recent")
    public ResponseEntity<RecentPopulation> getRecentPopulation(@PathVariable String regionId) {
        RecentPopulation response = populationService.queryRecentPopulation(regionId);
        return ResponseEntity.ok(response);
    }
}