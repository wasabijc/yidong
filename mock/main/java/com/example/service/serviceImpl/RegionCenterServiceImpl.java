package com.example.service.serviceImpl;

import com.example.entity.po.RegionCenterEntity;
import com.example.entity.query.RegionCenterQuery;
import com.example.service.RegionCenterService;
import com.example.entity.query.SimplePage;
import com.example.enums.PageSizeEnum;
import com.example.mapper.RegionCenterMapper;
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
public class RegionCenterServiceImpl implements RegionCenterService
{
	@Resource
	private RegionCenterMapper<RegionCenterEntity,RegionCenterQuery> regionCenterMapper;

	/**
	 * 根据条件查询列表
	 */
	public List<RegionCenterEntity> findListByQuery(RegionCenterQuery param)
	{
		return regionCenterMapper.selectList(param);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(RegionCenterQuery param)
	{
		return regionCenterMapper.selectCount(param);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<RegionCenterEntity> findListByPage(RegionCenterQuery param)
	{
		Integer count = findCountByQuery(param);
		Integer pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
		Integer pageIndex = param.getPageIndex();

		SimplePage page = new SimplePage(pageIndex, count, pageSize);
		param.setSimplePage(page);
		List<RegionCenterEntity> list = findListByQuery(param);
		PaginationResultVO<RegionCenterEntity> resultVO = new PaginationResultVO<>(count, page.getPageSize(), page.getPageIndex(), page.getPageTotal(), list);
		return resultVO;
	}

	/**
	 * 新增
	 */
	public Integer add(RegionCenterEntity param)
	{
		return regionCenterMapper.insert(param);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<RegionCenterEntity> beanList)
	{
		return regionCenterMapper.insertBatch(beanList);
	}

	/**
	 * 新增/修改
	 */
	public Integer addOrModify(RegionCenterEntity param)
	{
		return regionCenterMapper.insertOrUpdate(param);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrModifyBatch(List<RegionCenterEntity> beanList)
	{
		return regionCenterMapper.insertOrUpdateBatch(beanList);
	}

	/**
	 * 根据RegionId查询
	 */
	public RegionCenterEntity getByRegionId(String regionId)
	{
		return regionCenterMapper.selectByRegionId(regionId);
	}

	/**
	 * 根据RegionId更新
	 */
	public Integer modifyByRegionId(RegionCenterEntity entity, String regionId)
	{
		return regionCenterMapper.updateByRegionId(entity, regionId);
	}

	/**
	 * 根据RegionId删除
	 */
	public Integer removeByRegionId(String regionId)
	{
		return regionCenterMapper.deleteByRegionId(regionId);
	}


}