package com.example.entity.po;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @description 
 * @since 2025-06-21
 * @author LingoJack
 */
public class CellInfoEntity implements Serializable
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

	public CellInfoEntity()
	{

	}

	public CellInfoEntity(String cellId, BigDecimal lat, BigDecimal lon)
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

	@Override
	public String toString()
	{
		return " 基站ID:" + (this.cellId == null ? "no value" : this.cellId) + " 纬度:" + (this.lat == null ? "no value" : this.lat) + " 经度:" + (this.lon == null ? "no value" : this.lon) ;
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

		public CellInfoEntity build()
		{
			return new CellInfoEntity(this);
		}
	}

	private CellInfoEntity(Builder builder)
	{
		this.cellId = builder.cellId;
		this.lat = builder.lat;
		this.lon = builder.lon;

	}
}