package com.example.controller;

import com.example.entity.po.RegionPopulationEntity;
import com.example.entity.query.RegionPopulationQuery;
import com.example.entity.vo.AgeDistributionVO;
import com.example.entity.vo.GenderDistributionVO;
import com.example.entity.vo.PopulationSummaryVO;
import com.example.service.RegionPopulationService;
import com.example.entity.vo.ResponseVO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Date;
import java.util.Map;

/**
 * @description Controller
 * @since 2025-06-21
 * @author LingoJack
 */
@RestController
@RequestMapping("/regionPopulation")
public class RegionPopulationController extends BaseController
{
	@Resource
	private RegionPopulationService regionPopulationServiceImpl;

	/**
	 * 查询区域人群统计摘要
	 */
	@RequestMapping("/summary")
	public ResponseVO getPopulationSummary(
			@RequestParam String regionId,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {

		// 设置默认时间范围（最近1小时）
		if (endTime == null) endTime = new Date();
		if (startTime == null) startTime = Date.from(Instant.now().minus(1, ChronoUnit.HOURS));

		PopulationSummaryVO summary = regionPopulationServiceImpl.getPopulationSummary(regionId, startTime, endTime);
		return ResponseVO.success(summary);
	}

	/**
	 * 查询年龄分布
	 */
	@RequestMapping("/age-distribution")
	public ResponseVO getAgeDistribution(
			@RequestParam String regionId,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {

		// 设置默认时间范围（最近1小时）
		if (endTime == null) endTime = new Date();
		if (startTime == null) startTime = Date.from(Instant.now().minus(1, ChronoUnit.HOURS));

		AgeDistributionVO distribution = regionPopulationServiceImpl.getAgeDistribution(regionId, startTime, endTime);
		return ResponseVO.success(distribution);
	}

	/**
	 * 查询性别分布
	 */
	@RequestMapping("/gender-distribution")
	public ResponseVO getGenderDistribution(
			@RequestParam String regionId,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {

		// 设置默认时间范围（最近1小时）
		if (endTime == null) endTime = new Date();
		if (startTime == null) startTime = Date.from(Instant.now().minus(1, ChronoUnit.HOURS));

		GenderDistributionVO distribution = regionPopulationServiceImpl.getGenderDistribution(regionId, startTime, endTime);
		return ResponseVO.success(distribution);
	}

	/**
	 * 查询最近5分钟的人群统计
	 */
	@RequestMapping("/recent")
	public ResponseVO getRecentPopulation(@RequestParam String regionId) {
		PopulationSummaryVO summary = regionPopulationServiceImpl.getRecentPopulationSummary(regionId, 5);
		return ResponseVO.success(summary);
	}

	/**
	 * Dify Agent接口：查询最近5分钟人群数量
	 */
	@RequestMapping("/dify-agent/recent")
	public ResponseVO difyAgentQueryRecentPopulation(@RequestBody Map<String, String> params) {
		String regionId = params.get("region_id");
		if (regionId == null || regionId.isEmpty()) {
			return ResponseVO.error("缺少区域ID参数");
		}

		PopulationSummaryVO summary = regionPopulationServiceImpl.getRecentPopulationSummary(regionId, 5);

		// 构建OpenAPI风格响应
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("region_id", regionId);
		result.put("time_window", summary.getTimeWindow());
		result.put("total_count", summary.getTotalCount());
		result.put("male_count", summary.getMaleCount());
		result.put("female_count", summary.getFemaleCount());

		Map<String, Integer> ageDistribution = new LinkedHashMap<>();
		ageDistribution.put("age_0_18", summary.getAge018());
		ageDistribution.put("age_19_25", summary.getAge1925());
		ageDistribution.put("age_26_35", summary.getAge2635());
		ageDistribution.put("age_36_45", summary.getAge3645());
		ageDistribution.put("age_46_55", summary.getAge4655());
		ageDistribution.put("age_56_plus", summary.getAge56Plus());
		result.put("age_distribution", ageDistribution);

		return ResponseVO.success(result);
	}

	/**
	 * 加载数据列表
	 */
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(@RequestBody RegionPopulationQuery query)
	{
		return SuccessResponse(regionPopulationServiceImpl.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("add")
	public ResponseVO add(RegionPopulationEntity param)
	{
		return SuccessResponse(regionPopulationServiceImpl.add(param));
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<RegionPopulationEntity> beanList)
	{
		return SuccessResponse(regionPopulationServiceImpl.addBatch(beanList));
	}

	/**
	 * 新增/修改
	 */
	@RequestMapping("addOrModify")
	public ResponseVO addOrModify(RegionPopulationEntity param)
	{
		return SuccessResponse(regionPopulationServiceImpl.addOrModify(param));
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("addOrModifyBatch")
	public ResponseVO addOrModifyBatch(@RequestBody List<RegionPopulationEntity> beanList)
	{
		return SuccessResponse(regionPopulationServiceImpl.addOrModifyBatch(beanList));
	}

	/**
	 * 根据Id查询
	 */
	@RequestMapping("getById")
	public ResponseVO getById(Long id)
	{
		return SuccessResponse(regionPopulationServiceImpl.getById(id));
	}

	/**
	 * 根据Id更新
	 */
	@RequestMapping("modifyById")
	public ResponseVO modifyById(RegionPopulationEntity entity, Long id)
	{
		return SuccessResponse(regionPopulationServiceImpl.modifyById(entity, id));
	}

	/**
	 * 根据Id删除
	 */
	@RequestMapping("removeById")
	public ResponseVO removeById(Long id)
	{
		return SuccessResponse(regionPopulationServiceImpl.removeById(id));
	}

	/**
	 * 根据RegionIdAndTimeWindow查询
	 */
	@RequestMapping("getByRegionIdAndTimeWindow")
	public ResponseVO getByRegionIdAndTimeWindow(String regionId, Date timeWindow)
	{
		return SuccessResponse(regionPopulationServiceImpl.getByRegionIdAndTimeWindow(regionId, timeWindow));
	}

	/**
	 * 根据RegionIdAndTimeWindow更新
	 */
	@RequestMapping("modifyByRegionIdAndTimeWindow")
	public ResponseVO modifyByRegionIdAndTimeWindow(RegionPopulationEntity entity, String regionId, Date timeWindow)
	{
		return SuccessResponse(regionPopulationServiceImpl.modifyByRegionIdAndTimeWindow(entity, regionId, timeWindow));
	}

	/**
	 * 根据RegionIdAndTimeWindow删除
	 */
	@RequestMapping("removeByRegionIdAndTimeWindow")
	public ResponseVO removeByRegionIdAndTimeWindow(String regionId, Date timeWindow)
	{
		return SuccessResponse(regionPopulationServiceImpl.removeByRegionIdAndTimeWindow(regionId, timeWindow));
	}


}