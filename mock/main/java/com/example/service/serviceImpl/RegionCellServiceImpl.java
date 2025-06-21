package com.example.service.serviceImpl;

import com.example.entity.po.RegionCellEntity;
import com.example.entity.query.RegionCellQuery;
import com.example.service.RegionCellService;
import com.example.entity.query.SimplePage;
import com.example.enums.PageSizeEnum;
import com.example.mapper.RegionCellMapper;
import com.example.entity.vo.PaginationResultVO;
import java.util.List;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @description ServiceImpl
 * @since 2025-06-21
 * @author LingoJack
 */
@Service
public class RegionCellServiceImpl implements RegionCellService
{
	@Resource
	private RegionCellMapper<RegionCellEntity,RegionCellQuery> regionCellMapper;

	/**
	 * 根据条件查询列表
	 */
	public List<RegionCellEntity> findListByQuery(RegionCellQuery param)
	{
		return regionCellMapper.selectList(param);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(RegionCellQuery param)
	{
		return regionCellMapper.selectCount(param);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<RegionCellEntity> findListByPage(RegionCellQuery param)
	{
		Integer count = findCountByQuery(param);
		Integer pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
		Integer pageIndex = param.getPageIndex();

		SimplePage page = new SimplePage(pageIndex, count, pageSize);
		param.setSimplePage(page);
		List<RegionCellEntity> list = findListByQuery(param);
		PaginationResultVO<RegionCellEntity> resultVO = new PaginationResultVO<>(count, page.getPageSize(), page.getPageIndex(), page.getPageTotal(), list);
		return resultVO;
	}

	/**
	 * 新增
	 */
	public Integer add(RegionCellEntity param)
	{
		return regionCellMapper.insert(param);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<RegionCellEntity> beanList)
	{
		return regionCellMapper.insertBatch(beanList);
	}

	/**
	 * 新增/修改
	 */
	public Integer addOrModify(RegionCellEntity param)
	{
		return regionCellMapper.insertOrUpdate(param);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrModifyBatch(List<RegionCellEntity> beanList)
	{
		return regionCellMapper.insertOrUpdateBatch(beanList);
	}

	/**
	 * 根据RegionIdAndCellId查询
	 */
	public RegionCellEntity getByRegionIdAndCellId(String regionId, String cellId)
	{
		return regionCellMapper.selectByRegionIdAndCellId(regionId, cellId);
	}

	/**
	 * 根据RegionIdAndCellId更新
	 */
	public Integer modifyByRegionIdAndCellId(RegionCellEntity entity, String regionId, String cellId)
	{
		return regionCellMapper.updateByRegionIdAndCellId(entity, regionId, cellId);
	}

	/**
	 * 根据RegionIdAndCellId删除
	 */
	public Integer removeByRegionIdAndCellId(String regionId, String cellId)
	{
		return regionCellMapper.deleteByRegionIdAndCellId(regionId, cellId);
	}


}