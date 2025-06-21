package com.example.controller;

import com.example.entity.po.RegionSnapshot;
import com.example.entity.po.User;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PopulationController {
    @Resource
    private MovementSimulator simulator;
    @Resource
    private RegionStatisticsEngine statisticsEngine;

    /**
     * 获取指定区域的实时人口数据
     * @param regionId 区域ID
     * @return RegionSnapshot 包含实时人口统计的快照对象
     * @apiNote 该接口从内存中直接计算实时数据，反映当前时刻的人口分布
     */
    @GetMapping("/realtime/{regionId}")
    public RegionSnapshot getRealtimePopulation(@PathVariable long regionId) {
        // 直接从内存计算实时数据
        Map<Long, List<User>> regionUsers = simulator.getCurrentRegionUsers();
        List<User> users = regionUsers.getOrDefault(regionId, Collections.emptyList());

        RegionSnapshot snapshot = new RegionSnapshot(regionId, Instant.now());
        snapshot.setTotalCount(users.size());

        // 统计性别和年龄分布
        for (User user : users) {
            if (user.getGender() == 1) snapshot.incrementMale();
            else snapshot.incrementFemale();

            if (user.getAge() < 20) snapshot.incrementAge10_20();
            else if (user.getAge() < 40) snapshot.incrementAge20_40();
            else snapshot.incrementAge40Plus();
        }

        return snapshot;
    }

    /**
     * 获取指定区域的历史人口数据
     * @param regionId 区域ID
     * @param start 开始时间(ISO 8601格式)
     * @param end 结束时间(ISO 8601格式)
     * @return List<RegionSnapshot> 历史人口快照列表
     * @apiNote 时间范围最大不超过30天，数据按时间升序排列
     */
    @GetMapping("/historical/{regionId}")
    public List<RegionSnapshot> getHistoricalPopulation(
            @PathVariable long regionId,
            @RequestParam String start,  // 改为String类型
            @RequestParam String end) {

        // 自定义解析逻辑
        Instant startTime = parseFlexibleDateTime(start);
        Instant endTime = parseFlexibleDateTime(end);

        return statisticsEngine.getHistoricalData(regionId, startTime, endTime);
    }

    private Instant parseFlexibleDateTime(String datetime) {
        try {
            // 尝试解析ISO格式（带T和Z）
            return Instant.parse(datetime);
        } catch (Exception e1) {
            try {
                // 尝试解析 YYYY-MM-DD HH:MM:SS 格式
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.systemDefault());
                return LocalDateTime.parse(datetime, formatter).toInstant(ZoneOffset.UTC);
            } catch (Exception e2) {
                throw new IllegalArgumentException("无效的时间格式，请使用: 2023-06-21T14:30:45Z 或 2023-06-21 14:30:45");
            }
        }
    }

    /**
     * 获取最近5分钟的人口数据(开放接口)
     * @param regionId 区域ID
     * @return Map<String, Object> 包含:
     *         - status: 请求状态("success"或"error")
     *         - data: 成功时包含人口数据(Map结构)
     *         - message: 错误时的错误信息
     * @apiNote 该接口为开放接口，无需认证，返回最近5分钟内的最新快照
     */
    @GetMapping("/open/last5min")
    public Map<String, Object> getLast5MinPopulation(@RequestParam long regionId) {
        Instant now = Instant.now();
        RegionSnapshot latestSnapshot = statisticsEngine.getLatestSnapshot(regionId);

        if (latestSnapshot != null &&
                latestSnapshot.getTimestamp().isAfter(now.minus(5, ChronoUnit.MINUTES))) {
            return Map.of(
                    "status", "success",
                    "data", Map.of(
                            "region_id", regionId,
                            "timestamp", latestSnapshot.getTimestamp().toString(),
                            "total_population", latestSnapshot.getTotalCount(),
                            "gender_distribution", Map.of(
                                    "male", latestSnapshot.getMaleCount(),
                                    "female", latestSnapshot.getFemaleCount()
                            ),
                            "age_distribution", Map.of(
                                    "age_10_20", latestSnapshot.getAge10_20(),
                                    "age_20_40", latestSnapshot.getAge20_40(),
                                    "age_40_plus", latestSnapshot.getAge40Plus()
                            )
                    )
            );
        }

        return Map.of(
                "status", "error",
                "message", "No data available for last 5 minutes"
        );
    }
}
