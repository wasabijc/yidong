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
public interface RegionCellMapper<Entity, Condition> extends BaseMapper<Entity, Condition>
{
	/**
	 * 根据RegionIdAndCellId查询
	 */
	Entity selectByRegionIdAndCellId(@Param("regionId") String regionId, @Param("cellId") String cellId);

	/**
	 * 根据RegionIdAndCellId更新
	 */
	Integer updateByRegionIdAndCellId(@Param("bean") Entity entity, @Param("regionId") String regionId, @Param("cellId") String cellId);

	/**
	 * 根据RegionIdAndCellId删除
	 */
	Integer deleteByRegionIdAndCellId(@Param("regionId") String regionId, @Param("cellId") String cellId);


}