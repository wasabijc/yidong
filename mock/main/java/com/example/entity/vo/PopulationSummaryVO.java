package com.example.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * 人群统计摘要视图对象
 */
@Data
public class PopulationSummaryVO {
    private String regionId;
    private Date timeWindow;
    private Date startTime;
    private Date endTime;
    private Integer totalCount;
    private Integer maleCount;
    private Integer femaleCount;
    private Integer age018;
    private Integer age1925;
    private Integer age2635;
    private Integer age3645;
    private Integer age4655;
    private Integer age56Plus;

}
