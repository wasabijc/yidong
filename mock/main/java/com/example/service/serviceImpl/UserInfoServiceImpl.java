package com.example.service.serviceImpl;

import com.example.entity.po.UserInfoEntity;
import com.example.entity.query.UserInfoQuery;
import com.example.service.UserInfoService;
import com.example.entity.query.SimplePage;
import com.example.enums.PageSizeEnum;
import com.example.mapper.UserInfoMapper;
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
public class UserInfoServiceImpl implements UserInfoService
{
	@Resource
	private UserInfoMapper<UserInfoEntity,UserInfoQuery> userInfoMapper;

	/**
	 * 根据条件查询列表
	 */
	public List<UserInfoEntity> findListByQuery(UserInfoQuery param)
	{
		return userInfoMapper.selectList(param);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(UserInfoQuery param)
	{
		return userInfoMapper.selectCount(param);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<UserInfoEntity> findListByPage(UserInfoQuery param)
	{
		Integer count = findCountByQuery(param);
		Integer pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
		Integer pageIndex = param.getPageIndex();

		SimplePage page = new SimplePage(pageIndex, count, pageSize);
		param.setSimplePage(page);
		List<UserInfoEntity> list = findListByQuery(param);
		PaginationResultVO<UserInfoEntity> resultVO = new PaginationResultVO<>(count, page.getPageSize(), page.getPageIndex(), page.getPageTotal(), list);
		return resultVO;
	}

	/**
	 * 新增
	 */
	public Integer add(UserInfoEntity param)
	{
		return userInfoMapper.insert(param);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<UserInfoEntity> beanList)
	{
		return userInfoMapper.insertBatch(beanList);
	}

	/**
	 * 新增/修改
	 */
	public Integer addOrModify(UserInfoEntity param)
	{
		return userInfoMapper.insertOrUpdate(param);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrModifyBatch(List<UserInfoEntity> beanList)
	{
		return userInfoMapper.insertOrUpdateBatch(beanList);
	}

	/**
	 * 根据Imsi查询
	 */
	public UserInfoEntity getByImsi(String imsi)
	{
		return userInfoMapper.selectByImsi(imsi);
	}

	/**
	 * 根据Imsi更新
	 */
	public Integer modifyByImsi(UserInfoEntity entity, String imsi)
	{
		return userInfoMapper.updateByImsi(entity, imsi);
	}

	/**
	 * 根据Imsi删除
	 */
	public Integer removeByImsi(String imsi)
	{
		return userInfoMapper.deleteByImsi(imsi);
	}


}