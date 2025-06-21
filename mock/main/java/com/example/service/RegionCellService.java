package com.example.service;

import com.example.entity.po.RegionCellEntity;
import com.example.entity.query.RegionCellQuery;
import java.util.List;
import com.example.entity.vo.PaginationResultVO;

/**
 * @description Service
 * @since 2025-06-21
 * @author LingoJack
 */
public interface RegionCellService
{
	/**
	 * 根据条件查询列表
	 */
	public List<RegionCellEntity> findListByQuery(RegionCellQuery param);

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(RegionCellQuery param);

	/**
	 * 分页查询
	 */
	public PaginationResultVO<RegionCellEntity> findListByPage(RegionCellQuery param);

	/**
	 * 新增
	 */
	public Integer add(RegionCellEntity param);

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<RegionCellEntity> beanList);

	/**
	 * 新增/修改
	 */
	public Integer addOrModify(RegionCellEntity param);

	/**
	 * 批量新增/修改
	 */
	public Integer addOrModifyBatch(List<RegionCellEntity> beanList);

	/**
	 * 根据RegionIdAndCellId查询
	 */
	public RegionCellEntity getByRegionIdAndCellId(String regionId, String cellId);

	/**
	 * 根据RegionIdAndCellId更新
	 */
	public Integer modifyByRegionIdAndCellId(RegionCellEntity entity, String regionId, String cellId);

	/**
	 * 根据RegionIdAndCellId删除
	 */
	public Integer removeByRegionIdAndCellId(String regionId, String cellId);


}