package com.example.entity.query;

/**
 * BaseParam实体类，作为基本的查询参数封装对象，包含了分页信息及排序需求。
 */
public class BaseQuery {
    /**
     * simplePage 的 pageIndex 和 pageSize 才是真正分页用的。
     * 因为前端上传的参数未必准确，后端会进行进一步校验，设置
     * 换句话说，simplePage 内部的才是真正的分页参数，其参考前端意见（此处的pageIndex,pageSize）并由后端进行进行设置
     */
    private SimplePage simplePage;

    /**
     * 当前页码，从1开始，由前端上传
     */
    private Integer pageIndex;

    /**
     * 每页记录数量，由前端上传
     */
    private Integer pageSize;

    /**
     * 排序字段，指定查询结果的排序依据，如"id DESC"
     */
    private String orderBy;

    /**
     * 获取分页信息对象。
     *
     * @return SimplePage 分页信息对象
     */
    public SimplePage getSimplePage() {
        return simplePage;
    }

    /**
     * 设置分页信息对象。
     *
     * @param simplePage 分页信息对象
     */
    public void setSimplePage(SimplePage simplePage) {
        this.simplePage = simplePage;
    }

    /**
     * 获取当前页码。
     *
     * @return Integer 当前页码
     */
    public Integer getPageIndex() {
        return pageIndex;
    }

    /**
     * 设置当前页码。
     *
     * @param pageIndex 当前页码
     */
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 获取每页记录数量。
     *
     * @return Integer 每页记录数量
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页记录数量。
     *
     * @param pageSize 每页记录数量
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取排序字段。
     *
     * @return String 排序字段，格式如："column ASC" 或 "column DESC"
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 设置排序方式
     *
     * @param sqlFieldName 要排序的数据库表的列字段名，在对应的Constant类有定义
     * @param order        顺序，在对应的Constant类有定义
     */
    public void setOrderBy(String sqlFieldName, String order) {
        this.orderBy = String.format("%s %s", sqlFieldName, order);
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
