package com.example.service.serviceImpl;

import com.example.entity.po.CellInfoEntity;
import com.example.entity.query.CellInfoQuery;
import com.example.service.CellInfoService;
import com.example.entity.query.SimplePage;
import com.example.enums.PageSizeEnum;
import com.example.mapper.CellInfoMapper;
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
public class CellInfoServiceImpl implements CellInfoService
{
	@Resource
	private CellInfoMapper<CellInfoEntity,CellInfoQuery> cellInfoMapper;

	/**
	 * 根据条件查询列表
	 */
	public List<CellInfoEntity> findListByQuery(CellInfoQuery param)
	{
		return cellInfoMapper.selectList(param);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(CellInfoQuery param)
	{
		return cellInfoMapper.selectCount(param);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<CellInfoEntity> findListByPage(CellInfoQuery param)
	{
		Integer count = findCountByQuery(param);
		Integer pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
		Integer pageIndex = param.getPageIndex();

		SimplePage page = new SimplePage(pageIndex, count, pageSize);
		param.setSimplePage(page);
		List<CellInfoEntity> list = findListByQuery(param);
		PaginationResultVO<CellInfoEntity> resultVO = new PaginationResultVO<>(count, page.getPageSize(), page.getPageIndex(), page.getPageTotal(), list);
		return resultVO;
	}

	/**
	 * 新增
	 */
	public Integer add(CellInfoEntity param)
	{
		return cellInfoMapper.insert(param);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<CellInfoEntity> beanList)
	{
		return cellInfoMapper.insertBatch(beanList);
	}

	/**
	 * 新增/修改
	 */
	public Integer addOrModify(CellInfoEntity param)
	{
		return cellInfoMapper.insertOrUpdate(param);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrModifyBatch(List<CellInfoEntity> beanList)
	{
		return cellInfoMapper.insertOrUpdateBatch(beanList);
	}

	/**
	 * 根据CellId查询
	 */
	public CellInfoEntity getByCellId(String cellId)
	{
		return cellInfoMapper.selectByCellId(cellId);
	}

	/**
	 * 根据CellId更新
	 */
	public Integer modifyByCellId(CellInfoEntity entity, String cellId)
	{
		return cellInfoMapper.updateByCellId(entity, cellId);
	}

	/**
	 * 根据CellId删除
	 */
	public Integer removeByCellId(String cellId)
	{
		return cellInfoMapper.deleteByCellId(cellId);
	}


}