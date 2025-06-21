package com.example.entity.query;

import java.math.BigDecimal;
import java.util.List;;


/**
 * @description 查询对象
 * @since 2025-06-21
 * @author LingoJack
 */
public class CellInfoQuery extends BaseQuery
{
	/**
	 * 基站ID
	 */
	private String cellId;

	/**
	 * 纬度
	 */
	private BigDecimal lat;

	/**
	 * 经度
	 */
	private BigDecimal lon;

	/**
	 * 支持模糊查询的基站ID
	 */
	private String cellIdFuzzy;

	/**
	 * 批量查询列表: 基站ID
	 */
	private List<String> cellIdList;

	public CellInfoQuery()
	{

	}

	public CellInfoQuery(String cellId, BigDecimal lat, BigDecimal lon)
	{
		this.cellId = cellId;
		this.lat = lat;
		this.lon = lon;
	}

	public void setCellId(String cellId)
	{
		this.cellId = cellId;
	}

	public String getCellId()
	{
		return this.cellId;
	}

	public void setLat(BigDecimal lat)
	{
		this.lat = lat;
	}

	public BigDecimal getLat()
	{
		return this.lat;
	}

	public void setLon(BigDecimal lon)
	{
		this.lon = lon;
	}

	public BigDecimal getLon()
	{
		return this.lon;
	}

	public void setCellIdFuzzy(String cellIdFuzzy)
	{
		this.cellIdFuzzy = cellIdFuzzy;
	}

	public String getCellIdFuzzy()
	{
		return this.cellIdFuzzy;
	}

	public void setCellIdList(List<String> cellIdList)
	{
		this.cellIdList = cellIdList;
	}

	public List<String> getCellIdList()
	{
		return this.cellIdList;
	}

	public static Builder builder()
	{
		 return new Builder();
	}

	private static class Builder
	{
		private String cellId;

		public Builder setCellId(String cellId)
		{
			this.cellId = cellId;
			return this;
		}

		private BigDecimal lat;

		public Builder setLat(BigDecimal lat)
		{
			this.lat = lat;
			return this;
		}

		private BigDecimal lon;

		public Builder setLon(BigDecimal lon)
		{
			this.lon = lon;
			return this;
		}

		private String cellIdFuzzy;

		public Builder setCellIdFuzzy(String cellIdFuzzy)
		{
			this.cellIdFuzzy = cellIdFuzzy;
			return this;
		}

		private List<String> cellIdList;

		public Builder setCellIdList(List<String> cellIdList)
		{
			this.cellIdList = cellIdList;
			return this;
		}

		public CellInfoQuery build()
		{
			return new CellInfoQuery(this);
		}
	}

	private CellInfoQuery(Builder builder)
	{
		this.cellId = builder.cellId;
		this.lat = builder.lat;
		this.lon = builder.lon;
		this.cellIdFuzzy = builder.cellIdFuzzy;
		this.cellIdList = builder.cellIdList;

	}
}