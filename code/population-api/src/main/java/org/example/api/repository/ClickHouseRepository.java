package org.example.api.repository;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ClickHouseRepository {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public ClickHouseRepository(
            @Value("${clickhouse.url}") String jdbcUrl,
            @Value("${clickhouse.username}") String username,
            @Value("${clickhouse.password}") String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    /**
     * 查询总人数
     */
    public long queryTotal(String regionId, LocalDateTime startTime, LocalDateTime endTime) {
        String search = "SELECT bitmapCardinality(total_bitmap) AS total_count FROM region_user_bitmaps " +
                "WHERE region_id = ? AND timestamp BETWEEN ? AND ? " +
                "ORDER BY timestamp DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement(search)) {

            stmt.setString(1, regionId);
            stmt.setObject(2, startTime);
            stmt.setObject(3, endTime);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to query total population: " + e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 查询年龄分布
     */
    public Map<String, Long> queryAgeDistribution(String regionId, LocalDateTime startTime, LocalDateTime endTime) {
        String search = "SELECT " +
                "sum(bitmapCardinality(male_bitmap)) AS male_count, " +
                "sum(bitmapCardinality(female_bitmap)) AS female_count " +
                "FROM region_5min_agg_mv " +
                "WHERE region_id = ? AND timestamp BETWEEN ? AND ?";
        return executeGroupQuery(search, regionId, startTime, endTime);
    }

    /**
     * 查询性别分布
     */
    public Map<String, Long> queryGenderDistribution(String regionId, LocalDateTime startTime, LocalDateTime endTime) {
        String search = "SELECT " +
                "sum(bitmapCardinality(male_bitmap)) AS male_count, " +
                "sum(bitmapCardinality(female_bitmap)) AS female_count " +
                "FROM region_5min_agg_mv " +
                "WHERE region_id = ? AND timestamp BETWEEN ? AND ?";
        return executeGroupQuery(search, regionId, startTime, endTime);
    }

    /**
     * 执行分组查询（年龄、性别）
     */
    private Map<String, Long> executeGroupQuery(String sql, String regionId, LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Long> result = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, regionId);
            stmt.setObject(2, startTime);
            stmt.setObject(3, endTime);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String key = rs.getString(1);
                long value = rs.getLong(2);
                result.put(key, value);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute group query: " + e.getMessage(), e);
        }
        return result;
    }
}