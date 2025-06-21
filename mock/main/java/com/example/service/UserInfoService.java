package com.example.service;

import com.example.entity.po.UserInfoEntity;
import com.example.entity.query.UserInfoQuery;
import java.util.List;
import com.example.entity.vo.PaginationResultVO;

/**
 * @description Service
 * @since 2025-06-21
 * @author LingoJack
 */
public interface UserInfoService
{
	/**
	 * 根据条件查询列表
	 */
	public List<UserInfoEntity> findListByQuery(UserInfoQuery param);

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByQuery(UserInfoQuery param);

	/**
	 * 分页查询
	 */
	public PaginationResultVO<UserInfoEntity> findListByPage(UserInfoQuery param);

	/**
	 * 新增
	 */
	public Integer add(UserInfoEntity param);

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<UserInfoEntity> beanList);

	/**
	 * 新增/修改
	 */
	public Integer addOrModify(UserInfoEntity param);

	/**
	 * 批量新增/修改
	 */
	public Integer addOrModifyBatch(List<UserInfoEntity> beanList);

	/**
	 * 根据Imsi查询
	 */
	public UserInfoEntity getByImsi(String imsi);

	/**
	 * 根据Imsi更新
	 */
	public Integer modifyByImsi(UserInfoEntity entity, String imsi);

	/**
	 * 根据Imsi删除
	 */
	public Integer removeByImsi(String imsi);


}