package com.example.entity.query;

import java.util.Date;
import java.util.List;;


/**
 * @description 查询对象
 * @since 2025-06-21
 * @author LingoJack
 */
public class RegionPopulationQuery extends BaseQuery
{
	private Long id;

	/**
	 * 区域ID
	 */
	private String regionId;

	/**
	 * 统计时间窗口
	 */
	private Date timeWindow;

	/**
	 * 总人数
	 */
	private Integer totalCount;

	/**
	 * 男性人数
	 */
	private Integer maleCount;

	/**
	 * 女性人数
	 */
	private Integer femaleCount;

	/**
	 * 0-18岁人数
	 */
	private Integer age018;

	/**
	 * 19-25岁人数
	 */
	private Integer age1925;

	/**
	 * 26-35岁人数
	 */
	private Integer age2635;

	/**
	 * 36-45岁人数
	 */
	private Integer age3645;

	/**
	 * 46-55岁人数
	 */
	private Integer age4655;

	/**
	 * 56岁以上人数
	 */
	private Integer age56Plus;

	/**
	 * 支持模糊查询的区域ID
	 */
	private String regionIdFuzzy;

	/**
	 * 支持根据起止日期（时间）查询的起始统计时间窗口
	 */
	private String timeWindowStart;

	/**
	 * 支持根据起止日期（时间）查询的截止统计时间窗口
	 */
	private String timeWindowEnd;

	/**
	 * 批量查询列表: 
	 */
	private List<Long> idList;

	public RegionPopulationQuery()
	{

	}

	public RegionPopulationQuery(Long id, String regionId, Date timeWindow, Integer totalCount, Integer maleCount, Integer femaleCount, Integer age018, Integer age1925, Integer age2635, Integer age3645, Integer age4655, Integer age56Plus)
	{
		this.id = id;
		this.regionId = regionId;
		this.timeWindow = timeWindow;
		this.totalCount = totalCount;
		this.maleCount = maleCount;
		this.femaleCount = femaleCount;
		this.age018 = age018;
		this.age1925 = age1925;
		this.age2635 = age2635;
		this.age3645 = age3645;
		this.age4655 = age4655;
		this.age56Plus = age56Plus;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getId()
	{
		return this.id;
	}

	public void setRegionId(String regionId)
	{
		this.regionId = regionId;
	}

	public String getRegionId()
	{
		return this.regionId;
	}

	public void setTimeWindow(Date timeWindow)
	{
		this.timeWindow = timeWindow;
	}

	public Date getTimeWindow()
	{
		return this.timeWindow;
	}

	public void setTotalCount(Integer totalCount)
	{
		this.totalCount = totalCount;
	}

	public Integer getTotalCount()
	{
		return this.totalCount;
	}

	public void setMaleCount(Integer maleCount)
	{
		this.maleCount = maleCount;
	}

	public Integer getMaleCount()
	{
		return this.maleCount;
	}

	public void setFemaleCount(Integer femaleCount)
	{
		this.femaleCount = femaleCount;
	}

	public Integer getFemaleCount()
	{
		return this.femaleCount;
	}

	public void setAge018(Integer age018)
	{
		this.age018 = age018;
	}

	public Integer getAge018()
	{
		return this.age018;
	}

	public void setAge1925(Integer age1925)
	{
		this.age1925 = age1925;
	}

	public Integer getAge1925()
	{
		return this.age1925;
	}

	public void setAge2635(Integer age2635)
	{
		this.age2635 = age2635;
	}

	public Integer getAge2635()
	{
		return this.age2635;
	}

	public void setAge3645(Integer age3645)
	{
		this.age3645 = age3645;
	}

	public Integer getAge3645()
	{
		return this.age3645;
	}

	public void setAge4655(Integer age4655)
	{
		this.age4655 = age4655;
	}

	public Integer getAge4655()
	{
		return this.age4655;
	}

	public void setAge56Plus(Integer age56Plus)
	{
		this.age56Plus = age56Plus;
	}

	public Integer getAge56Plus()
	{
		return this.age56Plus;
	}

	public void setRegionIdFuzzy(String regionIdFuzzy)
	{
		this.regionIdFuzzy = regionIdFuzzy;
	}

	public String getRegionIdFuzzy()
	{
		return this.regionIdFuzzy;
	}

	public void setTimeWindowStart(String timeWindowStart)
	{
		this.timeWindowStart = timeWindowStart;
	}

	public String getTimeWindowStart()
	{
		return this.timeWindowStart;
	}

	public void setTimeWindowEnd(String timeWindowEnd)
	{
		this.timeWindowEnd = timeWindowEnd;
	}

	public String getTimeWindowEnd()
	{
		return this.timeWindowEnd;
	}

	public void setIdList(List<Long> idList)
	{
		this.idList = idList;
	}

	public List<Long> getIdList()
	{
		return this.idList;
	}

	public static Builder builder()
	{
		 return new Builder();
	}

	private static class Builder
	{
		private Long id;

		public Builder setId(Long id)
		{
			this.id = id;
			return this;
		}

		private String regionId;

		public Builder setRegionId(String regionId)
		{
			this.regionId = regionId;
			return this;
		}

		private Date timeWindow;

		public Builder setTimeWindow(Date timeWindow)
		{
			this.timeWindow = timeWindow;
			return this;
		}

		private Integer totalCount;

		public Builder setTotalCount(Integer totalCount)
		{
			this.totalCount = totalCount;
			return this;
		}

		private Integer maleCount;

		public Builder setMaleCount(Integer maleCount)
		{
			this.maleCount = maleCount;
			return this;
		}

		private Integer femaleCount;

		public Builder setFemaleCount(Integer femaleCount)
		{
			this.femaleCount = femaleCount;
			return this;
		}

		private Integer age018;

		public Builder setAge018(Integer age018)
		{
			this.age018 = age018;
			return this;
		}

		private Integer age1925;

		public Builder setAge1925(Integer age1925)
		{
			this.age1925 = age1925;
			return this;
		}

		private Integer age2635;

		public Builder setAge2635(Integer age2635)
		{
			this.age2635 = age2635;
			return this;
		}

		private Integer age3645;

		public Builder setAge3645(Integer age3645)
		{
			this.age3645 = age3645;
			return this;
		}

		private Integer age4655;

		public Builder setAge4655(Integer age4655)
		{
			this.age4655 = age4655;
			return this;
		}

		private Integer age56Plus;

		public Builder setAge56Plus(Integer age56Plus)
		{
			this.age56Plus = age56Plus;
			return this;
		}

		private String regionIdFuzzy;

		public Builder setRegionIdFuzzy(String regionIdFuzzy)
		{
			this.regionIdFuzzy = regionIdFuzzy;
			return this;
		}

		private String timeWindowStart;

		public Builder setTimeWindowStart(String timeWindowStart)
		{
			this.timeWindowStart = timeWindowStart;
			return this;
		}

		private String timeWindowEnd;

		public Builder setTimeWindowEnd(String timeWindowEnd)
		{
			this.timeWindowEnd = timeWindowEnd;
			return this;
		}

		private List<Long> idList;

		public Builder setIdList(List<Long> idList)
		{
			this.idList = idList;
			return this;
		}

		public RegionPopulationQuery build()
		{
			return new RegionPopulationQuery(this);
		}
	}

	private RegionPopulationQuery(Builder builder)
	{
		this.id = builder.id;
		this.regionId = builder.regionId;
		this.timeWindow = builder.timeWindow;
		this.totalCount = builder.totalCount;
		this.maleCount = builder.maleCount;
		this.femaleCount = builder.femaleCount;
		this.age018 = builder.age018;
		this.age1925 = builder.age1925;
		this.age2635 = builder.age2635;
		this.age3645 = builder.age3645;
		this.age4655 = builder.age4655;
		this.age56Plus = builder.age56Plus;
		this.regionIdFuzzy = builder.regionIdFuzzy;
		this.timeWindowStart = builder.timeWindowStart;
		this.timeWindowEnd = builder.timeWindowEnd;
		this.idList = builder.idList;

	}
}