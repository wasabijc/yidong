package com.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Date;

/**
 * @description Mapper
 * @since 2025-06-21
 * @author LingoJack
 */
@Mapper
public interface RegionPopulationMapper<Entity, Condition> extends BaseMapper<Entity, Condition>
{
	/**
	 * 根据Id查询
	 */
	Entity selectById(@Param("id") Long id);

	/**
	 * 根据Id更新
	 */
	Integer updateById(@Param("bean") Entity entity, @Param("id") Long id);

	/**
	 * 根据Id删除
	 */
	Integer deleteById(@Param("id") Long id);

	/**
	 * 根据IdList删除
	 */
	List<Entity> selectByIdList(@Param("idList") List<Long> idList);

	/**
	 * 根据RegionIdAndTimeWindow查询
	 */
	Entity selectByRegionIdAndTimeWindow(@Param("regionId") String regionId, @Param("timeWindow") Date timeWindow);

	/**
	 * 根据RegionIdAndTimeWindow更新
	 */
	Integer updateByRegionIdAndTimeWindow(@Param("bean") Entity entity, @Param("regionId") String regionId, @Param("timeWindow") Date timeWindow);

	/**
	 * 根据RegionIdAndTimeWindow删除
	 */
	Integer deleteByRegionIdAndTimeWindow(@Param("regionId") String regionId, @Param("timeWindow") Date timeWindow);


}