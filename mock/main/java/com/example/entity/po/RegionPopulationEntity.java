package com.example.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.example.utils.DateUtils;
import com.example.enums.DateFormatEnum;


/**
 * @description 
 * @since 2025-06-21
 * @author LingoJack
 */
public class RegionPopulationEntity implements Serializable
{
	private Long id;

	/**
	 * 区域ID
	 */
	private String regionId;

	/**
	 * 统计时间窗口
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

	public RegionPopulationEntity()
	{

	}

	public RegionPopulationEntity(Long id, String regionId, Date timeWindow, Integer totalCount, Integer maleCount, Integer femaleCount, Integer age018, Integer age1925, Integer age2635, Integer age3645, Integer age4655, Integer age56Plus)
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

	@Override
	public String toString()
	{
		return " :" + (this.id == null ? "no value" : this.id) + " 区域ID:" + (this.regionId == null ? "no value" : this.regionId) + " 统计时间窗口:" + (this.timeWindow == null ? "no value" : DateUtils.format(this.timeWindow,DateUtils.PATTERN_DATE_TIME)) + " 总人数:" + (this.totalCount == null ? "no value" : this.totalCount) + " 男性人数:" + (this.maleCount == null ? "no value" : this.maleCount) + " 女性人数:" + (this.femaleCount == null ? "no value" : this.femaleCount) + " 0-18岁人数:" + (this.age018 == null ? "no value" : this.age018) + " 19-25岁人数:" + (this.age1925 == null ? "no value" : this.age1925) + " 26-35岁人数:" + (this.age2635 == null ? "no value" : this.age2635) + " 36-45岁人数:" + (this.age3645 == null ? "no value" : this.age3645) + " 46-55岁人数:" + (this.age4655 == null ? "no value" : this.age4655) + " 56岁以上人数:" + (this.age56Plus == null ? "no value" : this.age56Plus) ;
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

		public RegionPopulationEntity build()
		{
			return new RegionPopulationEntity(this);
		}
	}

	private RegionPopulationEntity(Builder builder)
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

	}
}