package org.example.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopulationResponse {
    private long total;
    private Map<String, Long> ageDistribution;
    private Map<String, Long> genderDistribution;
    private LocalDateTime timestamp;
}