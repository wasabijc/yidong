package com.example.controller;

import com.example.entity.po.UserInfoEntity;
import com.example.entity.query.UserInfoQuery;
import com.example.service.UserInfoService;
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
@RequestMapping("/userInfo")
public class UserInfoController extends BaseController
{
	@Resource
	private UserInfoService userInfoServiceImpl;

	/**
	 * 加载数据列表
	 */
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(@RequestBody UserInfoQuery query)
	{
		return SuccessResponse(userInfoServiceImpl.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("add")
	public ResponseVO add(UserInfoEntity param)
	{
		return SuccessResponse(userInfoServiceImpl.add(param));
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<UserInfoEntity> beanList)
	{
		return SuccessResponse(userInfoServiceImpl.addBatch(beanList));
	}

	/**
	 * 新增/修改
	 */
	@RequestMapping("addOrModify")
	public ResponseVO addOrModify(UserInfoEntity param)
	{
		return SuccessResponse(userInfoServiceImpl.addOrModify(param));
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("addOrModifyBatch")
	public ResponseVO addOrModifyBatch(@RequestBody List<UserInfoEntity> beanList)
	{
		return SuccessResponse(userInfoServiceImpl.addOrModifyBatch(beanList));
	}

	/**
	 * 根据Imsi查询
	 */
	@RequestMapping("getByImsi")
	public ResponseVO getByImsi(String imsi)
	{
		return SuccessResponse(userInfoServiceImpl.getByImsi(imsi));
	}

	/**
	 * 根据Imsi更新
	 */
	@RequestMapping("modifyByImsi")
	public ResponseVO modifyByImsi(UserInfoEntity entity, String imsi)
	{
		return SuccessResponse(userInfoServiceImpl.modifyByImsi(entity, imsi));
	}

	/**
	 * 根据Imsi删除
	 */
	@RequestMapping("removeByImsi")
	public ResponseVO removeByImsi(String imsi)
	{
		return SuccessResponse(userInfoServiceImpl.removeByImsi(imsi));
	}


}