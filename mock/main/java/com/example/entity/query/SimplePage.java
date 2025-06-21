package com.example.entity.query;

import com.example.enums.PageSizeEnum;

/**
 * SimplePage实体类，用于封装分页查询相关信息。
 */
public class SimplePage
{
    private int pageIndex; // 当前页码，从1开始
    private int entryNum;  // 总条目数
    private int pageSize;  // 每页显示的条目数
    private int pageTotal; // 总页数
    private int start;     // 分页查询起始位置

    /**
     * 默认构造函数。
     */
    public SimplePage()
    {

    }

    /**
     * 带参构造函数，根据页码、总条目数和每页大小初始化分页信息。
     *
     * @param pageIndex 当前页码
     * @param entryNum  总条目数
     * @param pageSize  每页显示的条目数
     */
    public SimplePage(Integer pageIndex, int entryNum, int pageSize)
    {
        if (pageIndex == null)
        {
            pageIndex = 0;
        }
        this.pageIndex = pageIndex;
        this.entryNum = entryNum;
        this.pageSize = pageSize;
        calculatePageDetails(); // 计算并设置分页的详细信息
    }

    /**
     * 另一个构造函数，直接根据查询的起始和页大小初始化。
     *
     * @param start    起始位置
     * @param pageSize 页大小
     */
    public SimplePage(int start, int pageSize)
    {
        this.start = start;
        this.pageSize = pageSize;
    }

    public int getPageIndex()
    {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex)
    {
        this.pageIndex = pageIndex;
    }

    public int getEntryNum()
    {
        return entryNum;
    }

    public void setEntryNum(int entryNum)
    {
        this.entryNum = entryNum;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public int getPageTotal()
    {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal)
    {
        this.pageTotal = pageTotal;
    }

    public int getStart()
    {
        return start;
    }

    public void setStart(int start)
    {
        this.start = start;
    }

    /**
     * 计算分页相关细节，如总页数、起始和结束索引等。
     * 确保分页参数的合理性，如页码、页大小等。
     */
    public void calculatePageDetails()
    {
        if (this.pageSize <= 0)
        {
            this.pageSize = PageSizeEnum.SIZE20.getSize(); // 如果未指定，使用默认页大小
        }

        if (entryNum > 0)
        {
            this.pageTotal = (this.entryNum % this.pageSize == 0 ?
                    this.entryNum / this.pageSize : (this.entryNum / this.pageSize) + 1);
        }
        else
        {
            pageTotal = 1; // 如果没有条目，默认为1页
        }

        // 确保页码在有效范围内
        this.pageIndex = Math.max(1, Math.min(this.pageIndex, this.pageTotal));

        this.start = pageSize * (pageIndex - 1);
    }
}
