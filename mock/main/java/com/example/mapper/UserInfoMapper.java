package com.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @description Mapper
 * @since 2025-06-21
 * @author LingoJack
 */
@Mapper
public interface UserInfoMapper<Entity, Condition> extends BaseMapper<Entity, Condition>
{
	/**
	 * 根据Imsi查询
	 */
	Entity selectByImsi(@Param("imsi") String imsi);

	/**
	 * 根据Imsi更新
	 */
	Integer updateByImsi(@Param("bean") Entity entity, @Param("imsi") String imsi);

	/**
	 * 根据Imsi删除
	 */
	Integer deleteByImsi(@Param("imsi") String imsi);

	/**
	 * 根据ImsiList删除
	 */
	List<Entity> selectByImsiList(@Param("imsiList") List<String> imsiList);


}