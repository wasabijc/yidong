package com.example.service;

import com.example.entity.po.CellInfoEntity;
import com.example.entity.query.CellInfoQuery;
import java.util.List;
import com.example.entity.vo.PaginationResultVO;

/**
 * @description Service
 * @since 2025-06-21
 * @author LingoJack
 */
public interface CellInfoService
{
	/**
	 * 根据条件查询列表
	 */
	public List<CellInfoEntity> findListByQuery(CellInfoQuery param);

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(CellInfoQuery param);

	/**
	 * 分页查询
	 */
	public PaginationResultVO<CellInfoEntity> findListByPage(CellInfoQuery param);

	/**
	 * 新增
	 */
	public Integer add(CellInfoEntity param);

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<CellInfoEntity> beanList);

	/**
	 * 新增/修改
	 */
	public Integer addOrModify(CellInfoEntity param);

	/**
	 * 批量新增/修改
	 */
	public Integer addOrModifyBatch(List<CellInfoEntity> beanList);

	/**
	 * 根据CellId查询
	 */
	public CellInfoEntity getByCellId(String cellId);

	/**
	 * 根据CellId更新
	 */
	public Integer modifyByCellId(CellInfoEntity entity, String cellId);

	/**
	 * 根据CellId删除
	 */
	public Integer removeByCellId(String cellId);


}