package com.example.service.serviceImpl;

import com.example.entity.po.RegionPopulationEntity;
import com.example.entity.query.RegionPopulationQuery;
import com.example.entity.vo.AgeDistributionVO;
import com.example.entity.vo.GenderDistributionVO;
import com.example.entity.vo.PopulationSummaryVO;
import com.example.service.RegionPopulationService;
import com.example.entity.query.SimplePage;
import com.example.enums.PageSizeEnum;
import com.example.mapper.RegionPopulationMapper;
import com.example.entity.vo.PaginationResultVO;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

import com.example.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * @description ServiceImpl
 * @since 2025-06-21
 * @author LingoJack
 */
@Service
public class RegionPopulationServiceImpl implements RegionPopulationService
{
	@Resource
	private RegionPopulationMapper<RegionPopulationEntity,RegionPopulationQuery> regionPopulationMapper;

	@Override
	public PopulationSummaryVO getPopulationSummary(String regionId, Date startTime, Date endTime) {
		// 构建查询条件
		RegionPopulationQuery query = new RegionPopulationQuery();
		query.setRegionId(regionId);
		query.setTimeWindowStart(DateUtils.format(startTime, DateUtils.PATTERN_DATE_TIME));
		query.setTimeWindowEnd(DateUtils.format(endTime, DateUtils.PATTERN_DATE_TIME));

		// 查询符合条件的所有记录
		List<RegionPopulationEntity> populations = findListByQuery(query);

		// 创建汇总VO并初始化
		PopulationSummaryVO summary = new PopulationSummaryVO();
		summary.setRegionId(regionId);
		summary.setStartTime(startTime);
		summary.setEndTime(endTime);

		if (populations.isEmpty()) {
			return summary;
		}

		// 设置时间窗口
		summary.setTimeWindow(populations.get(populations.size() - 1).getTimeWindow());

		// 聚合统计数据
		for (RegionPopulationEntity entity : populations) {
			summary.setTotalCount(summary.getTotalCount() + entity.getTotalCount());
			summary.setMaleCount(summary.getMaleCount() + entity.getMaleCount());
			summary.setFemaleCount(summary.getFemaleCount() + entity.getFemaleCount());
			summary.setAge018(summary.getAge018() + entity.getAge018());
			summary.setAge1925(summary.getAge1925() + entity.getAge1925());
			summary.setAge2635(summary.getAge2635() + entity.getAge2635());
			summary.setAge3645(summary.getAge3645() + entity.getAge3645());
			summary.setAge4655(summary.getAge4655() + entity.getAge4655());
			summary.setAge56Plus(summary.getAge56Plus() + entity.getAge56Plus());
		}

		return summary;
	}

	@Override
	public PopulationSummaryVO getRecentPopulationSummary(String regionId, int minutes) {
		Date now = new Date();
		Date startTime = Date.from(Instant.now().minus(minutes, ChronoUnit.MINUTES));
		return getPopulationSummary(regionId, startTime, now);
	}

	@Override
	public AgeDistributionVO getAgeDistribution(String regionId, Date startTime, Date endTime) {
		PopulationSummaryVO summary = getPopulationSummary(regionId, startTime, endTime);

		AgeDistributionVO distribution = new AgeDistributionVO();
		distribution.setAge018(summary.getAge018());
		distribution.setAge1925(summary.getAge1925());
		distribution.setAge2635(summary.getAge2635());
		distribution.setAge3645(summary.getAge3645());
		distribution.setAge4655(summary.getAge4655());
		distribution.setAge56Plus(summary.getAge56Plus());

		return distribution;
	}

	@Override
	public GenderDistributionVO getGenderDistribution(String regionId, Date startTime, Date endTime) {
		PopulationSummaryVO summary = getPopulationSummary(regionId, startTime, endTime);

		GenderDistributionVO distribution = new GenderDistributionVO();
		distribution.setMaleCount(summary.getMaleCount());
		distribution.setFemaleCount(summary.getFemaleCount());

		return distribution;
	}

	/**
	 * 获取当前的5分钟时间窗口
	 * @return 时间窗口Date对象
	 */
	private Date getCurrent5MinuteWindow() {
		Calendar cal = Calendar.getInstance();
		int minute = cal.get(Calendar.MINUTE);
		cal.set(Calendar.MINUTE, (minute / 5) * 5);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 根据条件查询列表
	 */
	public List<RegionPopulationEntity> findListByQuery(RegionPopulationQuery param)
	{
		return regionPopulationMapper.selectList(param);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(RegionPopulationQuery param)
	{
		return regionPopulationMapper.selectCount(param);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<RegionPopulationEntity> findListByPage(RegionPopulationQuery param)
	{
		Integer count = findCountByQuery(param);
		Integer pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
		Integer pageIndex = param.getPageIndex();

		SimplePage page = new SimplePage(pageIndex, count, pageSize);
		param.setSimplePage(page);
		List<RegionPopulationEntity> list = findListByQuery(param);
		PaginationResultVO<RegionPopulationEntity> resultVO = new PaginationResultVO<>(count, page.getPageSize(), page.getPageIndex(), page.getPageTotal(), list);
		return resultVO;
	}

	/**
	 * 新增
	 */
	public Integer add(RegionPopulationEntity param)
	{
		return regionPopulationMapper.insert(param);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<RegionPopulationEntity> beanList)
	{
		return regionPopulationMapper.insertBatch(beanList);
	}

	/**
	 * 新增/修改
	 */
	public Integer addOrModify(RegionPopulationEntity param)
	{
		return regionPopulationMapper.insertOrUpdate(param);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrModifyBatch(List<RegionPopulationEntity> beanList)
	{
		return regionPopulationMapper.insertOrUpdateBatch(beanList);
	}

	/**
	 * 根据Id查询
	 */
	public RegionPopulationEntity getById(Long id)
	{
		return regionPopulationMapper.selectById(id);
	}

	/**
	 * 根据Id更新
	 */
	public Integer modifyById(RegionPopulationEntity entity, Long id)
	{
		return regionPopulationMapper.updateById(entity, id);
	}

	/**
	 * 根据Id删除
	 */
	public Integer removeById(Long id)
	{
		return regionPopulationMapper.deleteById(id);
	}

	/**
	 * 根据RegionIdAndTimeWindow查询
	 */
	public RegionPopulationEntity getByRegionIdAndTimeWindow(String regionId, Date timeWindow)
	{
		return regionPopulationMapper.selectByRegionIdAndTimeWindow(regionId, timeWindow);
	}

	/**
	 * 根据RegionIdAndTimeWindow更新
	 */
	public Integer modifyByRegionIdAndTimeWindow(RegionPopulationEntity entity, String regionId, Date timeWindow)
	{
		return regionPopulationMapper.updateByRegionIdAndTimeWindow(entity, regionId, timeWindow);
	}

	/**
	 * 根据RegionIdAndTimeWindow删除
	 */
	public Integer removeByRegionIdAndTimeWindow(String regionId, Date timeWindow)
	{
		return regionPopulationMapper.deleteByRegionIdAndTimeWindow(regionId, timeWindow);
	}


}