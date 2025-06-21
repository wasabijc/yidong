package com.example.service.serviceImpl;

import com.example.entity.po.CellInfoEntity;
import com.example.entity.po.RegionCellEntity;
import com.example.entity.po.RegionCenterEntity;
import com.example.entity.po.UserInfoEntity;
import com.example.entity.query.CellInfoQuery;
import com.example.entity.query.RegionCellQuery;
import com.example.entity.query.RegionCenterQuery;
import com.example.entity.query.UserInfoQuery;
import com.example.mapper.CellInfoMapper;
import com.example.mapper.RegionCellMapper;
import com.example.mapper.RegionCenterMapper;
import com.example.mapper.UserInfoMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataInitializationService {

    @Autowired
    private UserInfoMapper<UserInfoEntity, UserInfoQuery> userInfoRepository;

    @Autowired
    private CellInfoMapper<CellInfoEntity, CellInfoQuery> cellInfoRepository;

    @Autowired
    private RegionCellMapper<RegionCellEntity, RegionCellQuery> regionCellRepository;

    @Autowired
    private RegionCenterMapper<RegionCenterEntity, RegionCenterQuery> regionCenterRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void init() {
        initUserInfo();
        initCellInfo();
        initRegionCell();
        initRegionCenter();
    }

    private void initUserInfo() {
        if (userInfoRepository.selectCount(new UserInfoQuery()) > 0) return;

        List<UserInfoEntity> users = new ArrayList<>();
        try {
            Resource resource = resourceLoader.getResource("classpath:data/user_info.loc");
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String imsi = parts[0];
                    int gender = Integer.parseInt(parts[1]);
                    int age = Integer.parseInt(parts[2]);
                    users.add(new UserInfoEntity(imsi, gender, age));
                }
            }

            if (!users.isEmpty()) {
                userInfoRepository.insertBatch(users);
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read user_info.loc", e);
        }
    }

    private void initCellInfo() {
        if (cellInfoRepository.selectCount(new CellInfoQuery()) > 0) return;

        List<CellInfoEntity> cells = new ArrayList<>();
        try {
            Resource resource = resourceLoader.getResource("classpath:data/cell.loc");
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String cellId = parts[0];
                    double latitude = Double.parseDouble(parts[1]);
                    double longitude = Double.parseDouble(parts[2]);
                    cells.add(new CellInfoEntity(cellId, new BigDecimal(latitude), new BigDecimal(longitude)));
                }
            }

            if (!cells.isEmpty()) {
                cellInfoRepository.insertBatch(cells);
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read cell.loc", e);
        }
    }

    private void initRegionCell() {
        if (regionCellRepository.selectCount(new RegionCellQuery()) > 0) return;

        List<RegionCellEntity> mappings = new ArrayList<>();
        try {
            Resource resource = resourceLoader.getResource("classpath:data/region.loc");
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String regionId = parts[0];
                    String cellId = parts[1];
                    mappings.add(new RegionCellEntity(regionId, cellId));
                }
            }

            if (!mappings.isEmpty()) {
                regionCellRepository.insertBatch(mappings);
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read region.loc", e);
        }
    }

    private void initRegionCenter() {
        if (regionCenterRepository.selectCount(new RegionCenterQuery()) > 0) return;

        List<RegionCenterEntity> centers = new ArrayList<>();
        try {
            Resource resource = resourceLoader.getResource("classpath:data/region_center_loc.loc");
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String regionId = parts[0];
                    String centerPoint = parts[1];
                    String polygonPoints = parts[2];
                    centers.add(new RegionCenterEntity(regionId, centerPoint, polygonPoints));
                }
            }

            if (!centers.isEmpty()) {
                regionCenterRepository.insertBatch(centers);
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read region_center_loc.loc", e);
        }
    }
}