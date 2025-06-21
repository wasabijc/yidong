package com.example.service;

import com.example.entity.po.RegionPopulationEntity;
import com.example.entity.query.RegionPopulationQuery;

import java.util.List;

import com.example.entity.vo.AgeDistributionVO;
import com.example.entity.vo.GenderDistributionVO;
import com.example.entity.vo.PaginationResultVO;
import com.example.entity.vo.PopulationSummaryVO;

import java.util.Date;

/**
 * @description Service
 * @since 2025-06-21
 * @author LingoJack
 */
public interface RegionPopulationService
{

	/**
	 * 获取区域人群统计摘要
	 * @param regionId 区域ID
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 人群统计摘要
	 */
	PopulationSummaryVO getPopulationSummary(String regionId, Date startTime, Date endTime);

	/**
	 * 获取最近N分钟的人群统计
	 * @param regionId 区域ID
	 * @param minutes 最近多少分钟
	 * @return 人群统计摘要
	 */
	PopulationSummaryVO getRecentPopulationSummary(String regionId, int minutes);

	/**
	 * 获取年龄分布数据
	 * @param regionId 区域ID
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 年龄分布数据
	 */
	AgeDistributionVO getAgeDistribution(String regionId, Date startTime, Date endTime);

	/**
	 * 获取性别分布数据
	 * @param regionId 区域ID
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 性别分布数据
	 */
	GenderDistributionVO getGenderDistribution(String regionId, Date startTime, Date endTime);

	/**
	 * 根据条件查询列表
	 */
	public List<RegionPopulationEntity> findListByQuery(RegionPopulationQuery param);

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(RegionPopulationQuery param);

	/**
	 * 分页查询
	 */
	public PaginationResultVO<RegionPopulationEntity> findListByPage(RegionPopulationQuery param);

	/**
	 * 新增
	 */
	public Integer add(RegionPopulationEntity param);

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<RegionPopulationEntity> beanList);

	/**
	 * 新增/修改
	 */
	public Integer addOrModify(RegionPopulationEntity param);

	/**
	 * 批量新增/修改
	 */
	public Integer addOrModifyBatch(List<RegionPopulationEntity> beanList);

	/**
	 * 根据Id查询
	 */
	public RegionPopulationEntity getById(Long id);

	/**
	 * 根据Id更新
	 */
	public Integer modifyById(RegionPopulationEntity entity, Long id);

	/**
	 * 根据Id删除
	 */
	public Integer removeById(Long id);

	/**
	 * 根据RegionIdAndTimeWindow查询
	 */
	public RegionPopulationEntity getByRegionIdAndTimeWindow(String regionId, Date timeWindow);

	/**
	 * 根据RegionIdAndTimeWindow更新
	 */
	public Integer modifyByRegionIdAndTimeWindow(RegionPopulationEntity entity, String regionId, Date timeWindow);

	/**
	 * 根据RegionIdAndTimeWindow删除
	 */
	public Integer removeByRegionIdAndTimeWindow(String regionId, Date timeWindow);


}