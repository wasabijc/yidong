package com.example.entity.vo;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * 分页结果值对象类，用于封装分页查询结果
 *
 * @param <T> 分页数据的类型
 */
public class PaginationResultVO<T> implements Serializable
{
    private Integer entryNum;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer pageTotal;
    private List<T> list = new ArrayList<>();

    /**
     * 默认构造函数
     */
    public PaginationResultVO()
    {
    }

    /**
     * 带参构造函数
     *
     * @param entryNum  总条目数
     * @param pageSize  每页大小
     * @param pageIndex 当前页码
     * @param pageTotal 总页数
     * @param list      当前页的数据列表
     */
    public PaginationResultVO(Integer entryNum, Integer pageSize, Integer pageIndex, Integer pageTotal, List<T> list)
    {
        if (pageIndex == 0)
        {
            pageIndex = 1;
        }
        this.entryNum = entryNum;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.pageTotal = pageTotal;
        this.list = list;
    }

    /**
     * 带参构造函数
     *
     * @param entryNum  总条目数
     * @param pageSize  每页大小
     * @param pageIndex 当前页码
     * @param list      当前页的数据列表
     */
    public PaginationResultVO(Integer entryNum, Integer pageSize, Integer pageIndex, List<T> list)
    {
        this.entryNum = entryNum;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.list = list;
    }

    /**
     * 带参构造函数
     *
     * @param list 当前页的数据列表
     */
    public PaginationResultVO(List<T> list)
    {
        this.list = list;
    }

    /**
     * 获取总条目数
     *
     * @return 总条目数
     */
    public Integer getEntryNum()
    {
        return entryNum;
    }

    /**
     * 设置总条目数
     *
     * @param entryNum 总条目数
     */
    public void setEntryNum(Integer entryNum)
    {
        this.entryNum = entryNum;
    }

    /**
     * 获取每页大小
     *
     * @return 每页大小
     */
    public Integer getPageSize()
    {
        return pageSize;
    }

    /**
     * 设置每页大小
     *
     * @param pageSize 每页大小
     */
    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * 获取当前页码
     *
     * @return 当前页码
     */
    public Integer getPageIndex()
    {
        return pageIndex;
    }

    /**
     * 设置当前页码
     *
     * @param pageIndex 当前页码
     */
    public void setPageIndex(Integer pageIndex)
    {
        this.pageIndex = pageIndex;
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public Integer getPageTotal()
    {
        return pageTotal;
    }

    /**
     * 设置总页数
     *
     * @param pageTotal 总页数
     */
    public void setPageTotal(Integer pageTotal)
    {
        this.pageTotal = pageTotal;
    }

    /**
     * 获取当前页的数据列表
     *
     * @return 数据列表
     */
    public List<T> getList()
    {
        return list;
    }

    /**
     * 设置当前页的数据列表
     *
     * @param list 数据列表
     */
    public void setList(List<T> list)
    {
        this.list = list;
    }
}
