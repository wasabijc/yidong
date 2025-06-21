package com.example.controller;

import com.example.entity.po.Cell;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.entity.po.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MovementSimulator {
    private final Random random = new Random();
    private final Map<String, Long> currentCellMap = new ConcurrentHashMap<>();
    private final Map<String, User> users;
    private final Map<Long, Cell> cells;
    private final List<Long> cellIds;
    private final Map<Long, Region> regions;


    public MovementSimulator(DataLoader dataLoader) {
        this.users = dataLoader.getUsers().stream()
                .collect(Collectors.toMap(User::getImsi, Function.identity()));
        this.cells = dataLoader.getCells();
        this.regions = dataLoader.getRegions();
        this.cellIds = new ArrayList<>(cells.keySet());
        Collections.sort(cellIds); // 按基站ID排序

        // 初始化用户位置
        for (User user : dataLoader.getUsers()) {
            currentCellMap.put(user.getImsi(), cellIds.get(random.nextInt(cellIds.size())));
        }
    }

    @Scheduled(fixedRate = 100) // 每100毫秒移动一个用户
    public void simulateMovement() {
        // 随机选择一个用户
        int randomIndex = random.nextInt(users.size());
        String imsi = new ArrayList<>(users.keySet()).get(randomIndex);

        // 获取当前基站索引
        long currentCellId = currentCellMap.get(imsi);
        int currentIndex = cellIds.indexOf(currentCellId);

        // 生成移动步长 (-3 到 3)
        int step = random.nextInt(7) - 3;

        if (step > 0) {
            // 生成可能的移动方向
            int index1 = Math.min(currentIndex + step, cellIds.size() - 1);
            int index2 = Math.max(currentIndex - step, 0);

            // 随机选择一个方向
            int newIndex = random.nextBoolean() ? index1 : index2;
            currentCellMap.put(imsi, cellIds.get(newIndex));
        }
    }

    public Map<Long, List<User>> getCurrentRegionUsers() {
        Map<Long, List<User>> regionUsers = new HashMap<>();

        for (Map.Entry<String, Long> entry : currentCellMap.entrySet()) {
            String imsi = entry.getKey();
            Long cellId = entry.getValue();
            User user = users.get(imsi);

            // 查找用户所属区域
            for (Region region : regions.values()) {
                if (region.getCellIds().contains(cellId)) {
                    regionUsers.computeIfAbsent(region.getRegionId(), k -> new ArrayList<>()).add(user);
                    break;
                }
            }
        }

        return regionUsers;
    }
}

