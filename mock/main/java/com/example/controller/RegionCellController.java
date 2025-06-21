package com.example.controller;

import com.example.entity.po.RegionCellEntity;
import com.example.entity.query.RegionCellQuery;
import com.example.service.RegionCellService;
import com.example.entity.vo.ResponseVO;
import java.util.List;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description Controller
 * @since 2025-06-21
 * @author LingoJack
 */
@RestController
@RequestMapping("/regionCell")
public class RegionCellController extends BaseController
{
	@Resource
	private RegionCellService regionCellServiceImpl;

	/**
	 * 加载数据列表
	 */
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(@RequestBody RegionCellQuery query)
	{
		return SuccessResponse(regionCellServiceImpl.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("add")
	public ResponseVO add(RegionCellEntity param)
	{
		return SuccessResponse(regionCellServiceImpl.add(param));
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<RegionCellEntity> beanList)
	{
		return SuccessResponse(regionCellServiceImpl.addBatch(beanList));
	}

	/**
	 * 新增/修改
	 */
	@RequestMapping("addOrModify")
	public ResponseVO addOrModify(RegionCellEntity param)
	{
		return SuccessResponse(regionCellServiceImpl.addOrModify(param));
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("addOrModifyBatch")
	public ResponseVO addOrModifyBatch(@RequestBody List<RegionCellEntity> beanList)
	{
		return SuccessResponse(regionCellServiceImpl.addOrModifyBatch(beanList));
	}

	/**
	 * 根据RegionIdAndCellId查询
	 */
	@RequestMapping("getByRegionIdAndCellId")
	public ResponseVO getByRegionIdAndCellId(String regionId, String cellId)
	{
		return SuccessResponse(regionCellServiceImpl.getByRegionIdAndCellId(regionId, cellId));
	}

	/**
	 * 根据RegionIdAndCellId更新
	 */
	@RequestMapping("modifyByRegionIdAndCellId")
	public ResponseVO modifyByRegionIdAndCellId(RegionCellEntity entity, String regionId, String cellId)
	{
		return SuccessResponse(regionCellServiceImpl.modifyByRegionIdAndCellId(entity, regionId, cellId));
	}

	/**
	 * 根据RegionIdAndCellId删除
	 */
	@RequestMapping("removeByRegionIdAndCellId")
	public ResponseVO removeByRegionIdAndCellId(String regionId, String cellId)
	{
		return SuccessResponse(regionCellServiceImpl.removeByRegionIdAndCellId(regionId, cellId));
	}


}