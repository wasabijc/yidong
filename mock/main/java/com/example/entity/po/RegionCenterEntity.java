package com.example.entity.po;

import java.io.Serializable;


/**
 * @description 
 * @since 2025-06-21
 * @author LingoJack
 */
public class RegionCenterEntity implements Serializable
{
	/**
	 * 区域ID
	 */
	private String regionId;

	/**
	 * 中心点坐标
	 */
	private String centerPoint;

	/**
	 * 多边形边界点
	 */
	private String polygonPoints;

	public RegionCenterEntity()
	{

	}

	public RegionCenterEntity(String regionId, String centerPoint, String polygonPoints)
	{
		this.regionId = regionId;
		this.centerPoint = centerPoint;
		this.polygonPoints = polygonPoints;
	}

	public void setRegionId(String regionId)
	{
		this.regionId = regionId;
	}

	public String getRegionId()
	{
		return this.regionId;
	}

	public void setCenterPoint(String centerPoint)
	{
		this.centerPoint = centerPoint;
	}

	public String getCenterPoint()
	{
		return this.centerPoint;
	}

	public void setPolygonPoints(String polygonPoints)
	{
		this.polygonPoints = polygonPoints;
	}

	public String getPolygonPoints()
	{
		return this.polygonPoints;
	}

	@Override
	public String toString()
	{
		return " 区域ID:" + (this.regionId == null ? "no value" : this.regionId) + " 中心点坐标:" + (this.centerPoint == null ? "no value" : this.centerPoint) + " 多边形边界点:" + (this.polygonPoints == null ? "no value" : this.polygonPoints) ;
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

		private String centerPoint;

		public Builder setCenterPoint(String centerPoint)
		{
			this.centerPoint = centerPoint;
			return this;
		}

		private String polygonPoints;

		public Builder setPolygonPoints(String polygonPoints)
		{
			this.polygonPoints = polygonPoints;
			return this;
		}

		public RegionCenterEntity build()
		{
			return new RegionCenterEntity(this);
		}
	}

	private RegionCenterEntity(Builder builder)
	{
		this.regionId = builder.regionId;
		this.centerPoint = builder.centerPoint;
		this.polygonPoints = builder.polygonPoints;

	}
}