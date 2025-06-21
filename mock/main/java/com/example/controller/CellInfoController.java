package com.example.controller;

import com.example.entity.po.CellInfoEntity;
import com.example.entity.query.CellInfoQuery;
import com.example.service.CellInfoService;
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
@RequestMapping("/cellInfo")
public class CellInfoController extends BaseController
{
	@Resource
	private CellInfoService cellInfoServiceImpl;

	/**
	 * 加载数据列表
	 */
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(@RequestBody CellInfoQuery query)
	{
		return SuccessResponse(cellInfoServiceImpl.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("add")
	public ResponseVO add(CellInfoEntity param)
	{
		return SuccessResponse(cellInfoServiceImpl.add(param));
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<CellInfoEntity> beanList)
	{
		return SuccessResponse(cellInfoServiceImpl.addBatch(beanList));
	}

	/**
	 * 新增/修改
	 */
	@RequestMapping("addOrModify")
	public ResponseVO addOrModify(CellInfoEntity param)
	{
		return SuccessResponse(cellInfoServiceImpl.addOrModify(param));
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("addOrModifyBatch")
	public ResponseVO addOrModifyBatch(@RequestBody List<CellInfoEntity> beanList)
	{
		return SuccessResponse(cellInfoServiceImpl.addOrModifyBatch(beanList));
	}

	/**
	 * 根据CellId查询
	 */
	@RequestMapping("getByCellId")
	public ResponseVO getByCellId(String cellId)
	{
		return SuccessResponse(cellInfoServiceImpl.getByCellId(cellId));
	}

	/**
	 * 根据CellId更新
	 */
	@RequestMapping("modifyByCellId")
	public ResponseVO modifyByCellId(CellInfoEntity entity, String cellId)
	{
		return SuccessResponse(cellInfoServiceImpl.modifyByCellId(entity, cellId));
	}

	/**
	 * 根据CellId删除
	 */
	@RequestMapping("removeByCellId")
	public ResponseVO removeByCellId(String cellId)
	{
		return SuccessResponse(cellInfoServiceImpl.removeByCellId(cellId));
	}


}