package com.example.controller;

import com.example.entity.po.Region;
import com.example.entity.po.RegionSnapshot;
import com.example.entity.po.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class RegionStatisticsEngine {
    private final Map<Long, Region> regions;
    private final MovementSimulator simulator;
    private final Deque<RegionSnapshot> snapshots = new ConcurrentLinkedDeque<>();

    public RegionStatisticsEngine(DataLoader dataLoader, MovementSimulator simulator) {
        this.regions = dataLoader.getRegions();
        this.simulator = simulator;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MICROSECONDS)
    public void generateSnapshots() {
        Instant now = Instant.now();
        Map<Long, List<User>> regionUsers = simulator.getCurrentRegionUsers();
        System.out.println("generate snapshot for " + regions.size() + " regions");
        for (Region region : regions.values()) {
            RegionSnapshot snapshot = new RegionSnapshot(region.getRegionId(), now);

            List<User> usersInRegion = regionUsers.getOrDefault(region.getRegionId(), Collections.emptyList());
            snapshot.setTotalCount(usersInRegion.size());

            for (User user : usersInRegion) {
                if (user.getGender() == 1) snapshot.incrementMale();
                else snapshot.incrementFemale();

                if (user.getAge() < 20) snapshot.incrementAge10_20();
                else if (user.getAge() < 40) snapshot.incrementAge20_40();
                else snapshot.incrementAge40Plus();
            }

            snapshots.addFirst(snapshot); // 添加到队列头部
        }
    }

    public RegionSnapshot getLatestSnapshot(long regionId) {
        for (RegionSnapshot snapshot : snapshots) {
            if (snapshot.getRegionId() == regionId) {
                return snapshot;
            }
        }
        return null;
    }

    public List<RegionSnapshot> getHistoricalData(long regionId, Instant start, Instant end) {
        return snapshots.stream()
                .filter(s -> s.getRegionId() == regionId)
                .filter(s -> !s.getTimestamp().isBefore(start) && !s.getTimestamp().isAfter(end))
                .collect(Collectors.toList());
    }
}

