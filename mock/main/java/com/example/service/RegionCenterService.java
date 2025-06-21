package com.example.service;

import com.example.entity.po.RegionCenterEntity;
import com.example.entity.query.RegionCenterQuery;
import java.util.List;
import com.example.entity.vo.PaginationResultVO;

/**
 * @description Service
 * @since 2025-06-21
 * @author LingoJack
 */
public interface RegionCenterService
{
	/**
	 * 根据条件查询列表
	 */
	public List<RegionCenterEntity> findListByQuery(RegionCenterQuery param);

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(RegionCenterQuery param);

	/**
	 * 分页查询
	 */
	public PaginationResultVO<RegionCenterEntity> findListByPage(RegionCenterQuery param);

	/**
	 * 新增
	 */
	public Integer add(RegionCenterEntity param);

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<RegionCenterEntity> beanList);

	/**
	 * 新增/修改
	 */
	public Integer addOrModify(RegionCenterEntity param);

	/**
	 * 批量新增/修改
	 */
	public Integer addOrModifyBatch(List<RegionCenterEntity> beanList);

	/**
	 * 根据RegionId查询
	 */
	public RegionCenterEntity getByRegionId(String regionId);

	/**
	 * 根据RegionId更新
	 */
	public Integer modifyByRegionId(RegionCenterEntity entity, String regionId);

	/**
	 * 根据RegionId删除
	 */
	public Integer removeByRegionId(String regionId);


}