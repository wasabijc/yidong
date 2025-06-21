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
public interface CellInfoMapper<Entity, Condition> extends BaseMapper<Entity, Condition>
{
	/**
	 * 根据CellId查询
	 */
	Entity selectByCellId(@Param("cellId") String cellId);

	/**
	 * 根据CellId更新
	 */
	Integer updateByCellId(@Param("bean") Entity entity, @Param("cellId") String cellId);

	/**
	 * 根据CellId删除
	 */
	Integer deleteByCellId(@Param("cellId") String cellId);

	/**
	 * 根据CellIdList删除
	 */
	List<Entity> selectByCellIdList(@Param("cellIdList") List<String> cellIdList);


}