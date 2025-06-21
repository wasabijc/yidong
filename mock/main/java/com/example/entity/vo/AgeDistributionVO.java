package com.example.entity.vo;

import lombok.Data;

/**
 * 年龄分布视图对象
 */
@Data
public class AgeDistributionVO {
    private Integer age018;
    private Integer age1925;
    private Integer age2635;
    private Integer age3645;
    private Integer age4655;
    private Integer age56Plus;

    // Getters and Setters...
}
