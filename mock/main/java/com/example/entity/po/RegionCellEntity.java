package com.example.entity.po;

import java.io.Serializable;


/**
 * @description 
 * @since 2025-06-21
 * @author LingoJack
 */
public class RegionCellEntity implements Serializable
{
	/**
	 * 区域ID
	 */
	private String regionId;

	/**
	 * 基站ID
	 */
	private String cellId;

	public RegionCellEntity()
	{

	}

	public RegionCellEntity(String regionId, String cellId)
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

	@Override
	public String toString()
	{
		return " 区域ID:" + (this.regionId == null ? "no value" : this.regionId) + " 基站ID:" + (this.cellId == null ? "no value" : this.cellId) ;
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

		public RegionCellEntity build()
		{
			return new RegionCellEntity(this);
		}
	}

	private RegionCellEntity(Builder builder)
	{
		this.regionId = builder.regionId;
		this.cellId = builder.cellId;

	}
}