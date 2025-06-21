package com.example.controller;

import com.example.entity.po.RegionCenterEntity;
import com.example.entity.query.RegionCenterQuery;
import com.example.service.RegionCenterService;
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
@RequestMapping("/regionCenter")
public class RegionCenterController extends BaseController
{
	@Resource
	private RegionCenterService regionCenterServiceImpl;

	/**
	 * 加载数据列表
	 */
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(@RequestBody RegionCenterQuery query)
	{
		return SuccessResponse(regionCenterServiceImpl.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("add")
	public ResponseVO add(RegionCenterEntity param)
	{
		return SuccessResponse(regionCenterServiceImpl.add(param));
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<RegionCenterEntity> beanList)
	{
		return SuccessResponse(regionCenterServiceImpl.addBatch(beanList));
	}

	/**
	 * 新增/修改
	 */
	@RequestMapping("addOrModify")
	public ResponseVO addOrModify(RegionCenterEntity param)
	{
		return SuccessResponse(regionCenterServiceImpl.addOrModify(param));
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("addOrModifyBatch")
	public ResponseVO addOrModifyBatch(@RequestBody List<RegionCenterEntity> beanList)
	{
		return SuccessResponse(regionCenterServiceImpl.addOrModifyBatch(beanList));
	}

	/**
	 * 根据RegionId查询
	 */
	@RequestMapping("getByRegionId")
	public ResponseVO getByRegionId(String regionId)
	{
		return SuccessResponse(regionCenterServiceImpl.getByRegionId(regionId));
	}

	/**
	 * 根据RegionId更新
	 */
	@RequestMapping("modifyByRegionId")
	public ResponseVO modifyByRegionId(RegionCenterEntity entity, String regionId)
	{
		return SuccessResponse(regionCenterServiceImpl.modifyByRegionId(entity, regionId));
	}

	/**
	 * 根据RegionId删除
	 */
	@RequestMapping("removeByRegionId")
	public ResponseVO removeByRegionId(String regionId)
	{
		return SuccessResponse(regionCenterServiceImpl.removeByRegionId(regionId));
	}


}