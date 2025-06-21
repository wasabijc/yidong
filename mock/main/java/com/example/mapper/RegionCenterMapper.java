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
public interface RegionCenterMapper<Entity, Condition> extends BaseMapper<Entity, Condition>
{
	/**
	 * 根据RegionId查询
	 */
	Entity selectByRegionId(@Param("regionId") String regionId);

	/**
	 * 根据RegionId更新
	 */
	Integer updateByRegionId(@Param("bean") Entity entity, @Param("regionId") String regionId);

	/**
	 * 根据RegionId删除
	 */
	Integer deleteByRegionId(@Param("regionId") String regionId);

	/**
	 * 根据RegionIdList删除
	 */
	List<Entity> selectByRegionIdList(@Param("regionIdList") List<String> regionIdList);


}