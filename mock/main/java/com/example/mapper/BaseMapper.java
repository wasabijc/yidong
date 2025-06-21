package com.example.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 定义基础的MyBatis Mapper接口，提供通用的数据库操作方法。<br>
 * 泛型Entity代表实体类型，Condition代表查询条件类型。<br>
 * 实体类型（Entity Type）。在这个上下文中，它指的是数据库表对应的Java对象类型<br>
 * 查询条件类型（Query Query Type）。这个类型通常用于定义查询时的过滤条件。
 */
public interface BaseMapper<Entity, Query> {

    /**
     * 单行插入(不允许手动插入主键)
     *
     * @param entity 要插入的实体对象
     * @return 受影响的行数
     */
    Integer insert(@Param("bean") Entity entity);

    /**
     * 插入或更新（只要存在任一唯一键冲突就更新，否则插入。无法更新主键，可以更新之外的唯一键）
     *
     * @param entity 要插入或更新的实体对象
     * @return 受影响的行数
     */
    Integer insertOrUpdate(@Param("bean") Entity entity);

    /**
     * 批量插入记录。
     *
     * @param list 实体对象的集合
     * @return 受影响的行数
     */
    Integer insertBatch(@Param("list") List<Entity> list);

    /**
     * 批量插入或更新记录。对于集合中的每一项记录，如果存在则更新，否则插入。
     *
     * @param list 实体对象的集合
     * @return 受影响的行数
     */
    Integer insertOrUpdateBatch(@Param("list") List<Entity> list);

    /**
     * 根据查询条件获取实体列表。
     *
     * @param queryQuery 查询条件对象
     * @return 实体对象的列表
     */
    List<Entity> selectList(@Param("query") Query queryQuery);

    /**
     * 根据查询条件计算记录总数。
     *
     * @param queryQuery 查询条件对象
     * @return 记录总数
     */
    Integer selectCount(@Param("query") Query queryQuery);
}
