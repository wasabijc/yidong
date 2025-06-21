package com.example.entity.query;

import java.util.List;;


/**
 * @description 查询对象
 * @since 2025-06-21
 * @author LingoJack
 */
public class RegionCellQuery extends BaseQuery
{
	/**
	 * 区域ID
	 */
	private String regionId;

	/**
	 * 基站ID
	 */
	private String cellId;

	/**
	 * 支持模糊查询的区域ID
	 */
	private String regionIdFuzzy;

	/**
	 * 支持模糊查询的基站ID
	 */
	private String cellIdFuzzy;

	public RegionCellQuery()
	{

	}

	public RegionCellQuery(String regionId, String cellId)
	{
		this.regionId = regionId;
		this.cellId = cellId;
	}

	public void setRegionId(String regionId)
	{
		this.regionId = regionId;
	}

	public String getRegionId()
	{
		return this.regionId;
	}

	public void setCellId(String cellId)
	{
		this.cellId = cellId;
	}

	public String getCellId()
	{
		return this.cellId;
	}

	public void setRegionIdFuzzy(String regionIdFuzzy)
	{
		this.regionIdFuzzy = regionIdFuzzy;
	}

	public String getRegionIdFuzzy()
	{
		return this.regionIdFuzzy;
	}

	public void setCellIdFuzzy(String cellIdFuzzy)
	{
		this.cellIdFuzzy = cellIdFuzzy;
	}

	public String getCellIdFuzzy()
	{
		return this.cellIdFuzzy;
	}

	public static Builder builder()
	{
		 return new Builder();
	}

	private static class Builder
	{
		private String regionId;

		public Builder setRegionId(String regionId)
		{
			this.regionId = regionId;
			return this;
		}

		private String cellId;

		public Builder setCellId(String cellId)
		{
			this.cellId = cellId;
			return this;
		}

		private String regionIdFuzzy;

		public Builder setRegionIdFuzzy(String regionIdFuzzy)
		{
			this.regionIdFuzzy = regionIdFuzzy;
			return this;
		}

		private String cellIdFuzzy;

		public Builder setCellIdFuzzy(String cellIdFuzzy)
		{
			this.cellIdFuzzy = cellIdFuzzy;
			return this;
		}

		public RegionCellQuery build()
		{
			return new RegionCellQuery(this);
		}
	}

	private RegionCellQuery(Builder builder)
	{
		this.regionId = builder.regionId;
		this.cellId = builder.cellId;
		this.regionIdFuzzy = builder.regionIdFuzzy;
		this.cellIdFuzzy = builder.cellIdFuzzy;

	}
}