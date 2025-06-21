package com.example.api.service;

import com.example.api.model.PopulationResponse;
import com.example.api.model.RecentPopulation;
import com.example.api.repository.ClickHouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PopulationServiceImpl implements PopulationService {

    private final ClickHouseRepository clickHouseRepo;
    private final RestTemplate restTemplate;
    private final String difyAgentUrl;

    public PopulationServiceImpl(
            ClickHouseRepository clickHouseRepo,
            RestTemplate restTemplate,
            @Value("${dify.agent.url}") String difyAgentUrl) {
        this.clickHouseRepo = clickHouseRepo;
        this.restTemplate = restTemplate;
        this.difyAgentUrl = difyAgentUrl;
    }

    @Override
    public PopulationResponse queryPopulation(String regionId, LocalDateTime startTime, LocalDateTime endTime) {
        long total = clickHouseRepo.queryTotal(regionId, startTime, endTime);
        Map<String, Long> ageDistribution = clickHouseRepo.queryAgeDistribution(regionId, startTime, endTime);
        Map<String, Long> genderDistribution = clickHouseRepo.queryGenderDistribution(regionId, startTime, endTime);

        return new PopulationResponse(total, ageDistribution, genderDistribution, LocalDateTime.now());
    }

    @Override
    public RecentPopulation queryRecentPopulation(String regionId) {
        try {
            String url = String.format("%s/api/dify/query?target=clickhouse&region=%s&range=5m",
                    difyAgentUrl, regionId);
            return restTemplate.getForObject(url, RecentPopulation.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch recent population data from Dify: " + e.getMessage(), e);
        }
    }
}