package com.example.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import com.example.entity.po.*;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataLoader {
    private List<User> users = new ArrayList<>();
    private Map<Long, Cell> cells = new HashMap<>();
    private Map<Long, Region> regions = new HashMap<>();

    private final ResourceLoader resourceLoader;

    public DataLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void loadData() throws IOException {
        // 加载用户数据
        loadUserData();

        // 加载基站数据
        loadCellData();

        // 加载区域数据
        loadRegionData();

        // 加载区域边界数据
        loadRegionPolygons();
    }

    private List<String> readResourceLines(String resourcePath) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + resourcePath);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    private void loadUserData() throws IOException {
        List<String> userLines = readResourceLines("user_info.txt");
        for (String line : userLines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 3) {
                String imsi = parts[0].trim();
                int gender = Integer.parseInt(parts[1].trim());
                int age = Integer.parseInt(parts[2].trim());
                users.add(new User(imsi, gender, age));
            }
        }
        System.out.println("Loaded " + users.size() + " users");
    }

    private void loadCellData() throws IOException {
        List<String> cellLines = readResourceLines("cell_loc.txt");
        for (String line : cellLines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 3) {
                long cellId = Long.parseLong(parts[0].trim());
                double lat = Double.parseDouble(parts[1].trim());
                double lon = Double.parseDouble(parts[2].trim());
                cells.put(cellId, new Cell(cellId, lat, lon));
            }
        }
        System.out.println("Loaded " + cells.size() + " cells");
    }

    private void loadRegionData() throws IOException {
        List<String> regionLines = readResourceLines("region_cell.txt");
        for (String line : regionLines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 2) {
                long regionId = Long.parseLong(parts[0].trim());
                long cellId = Long.parseLong(parts[1].trim());
                System.out.println("regionId: " + regionId);
                regions.computeIfAbsent(regionId, id ->
                        new Region(id, "区域" + id, new ArrayList<>()));

                regions.get(regionId).getCellIds().add(cellId);
            }
        }
        System.out.println("Loaded " + regions.size() + " regions with " +
                regions.values().stream().mapToInt(r -> r.getCellIds().size()).sum() + " cells");
    }

    private void loadRegionPolygons() throws IOException {
        List<String> polygonLines = readResourceLines("region_center_loc.txt");
        for (String line : polygonLines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 3) {
                long regionId = Long.parseLong(parts[0].trim());
                // parts[1] 是中心点，我们不需要
                String polygonStr = parts[2].trim();

                String[] points = polygonStr.split(";");
                List<Point> polygon = Arrays.stream(points)
                        .map(pointStr -> {
                            String[] coords = pointStr.split(",");
                            double lon = Double.parseDouble(coords[0].trim());
                            double lat = Double.parseDouble(coords[1].trim());
                            return new Point(lon, lat);
                        })
                        .collect(Collectors.toList());

                Region region = regions.get(regionId);
                if (region != null) {
                    region.setPolygon(polygon);
                }
            }
        }
        System.out.println("Loaded polygons for " +
                regions.values().stream().filter(r -> r.getPolygon() != null).count() + " regions");
    }

    public List<User> getUsers() { return Collections.unmodifiableList(users); }
    public Map<Long, Cell> getCells() { return Collections.unmodifiableMap(cells); }
    public Map<Long, Region> getRegions() { return Collections.unmodifiableMap(regions); }
}