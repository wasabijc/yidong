package com.example.entity.query;

import java.util.List;;


/**
 * @description 查询对象
 * @since 2025-06-21
 * @author LingoJack
 */
public class RegionCenterQuery extends BaseQuery
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

	/**
	 * 支持模糊查询的区域ID
	 */
	private String regionIdFuzzy;

	/**
	 * 支持模糊查询的中心点坐标
	 */
	private String centerPointFuzzy;

	/**
	 * 支持模糊查询的多边形边界点
	 */
	private String polygonPointsFuzzy;

	/**
	 * 批量查询列表: 区域ID
	 */
	private List<String> regionIdList;

	public RegionCenterQuery()
	{

	}

	public RegionCenterQuery(String regionId, String centerPoint, String polygonPoints)
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

	public void setRegionIdFuzzy(String regionIdFuzzy)
	{
		this.regionIdFuzzy = regionIdFuzzy;
	}

	public String getRegionIdFuzzy()
	{
		return this.regionIdFuzzy;
	}

	public void setCenterPointFuzzy(String centerPointFuzzy)
	{
		this.centerPointFuzzy = centerPointFuzzy;
	}

	public String getCenterPointFuzzy()
	{
		return this.centerPointFuzzy;
	}

	public void setPolygonPointsFuzzy(String polygonPointsFuzzy)
	{
		this.polygonPointsFuzzy = polygonPointsFuzzy;
	}

	public String getPolygonPointsFuzzy()
	{
		return this.polygonPointsFuzzy;
	}

	public void setRegionIdList(List<String> regionIdList)
	{
		this.regionIdList = regionIdList;
	}

	public List<String> getRegionIdList()
	{
		return this.regionIdList;
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

		private String regionIdFuzzy;

		public Builder setRegionIdFuzzy(String regionIdFuzzy)
		{
			this.regionIdFuzzy = regionIdFuzzy;
			return this;
		}

		private String centerPointFuzzy;

		public Builder setCenterPointFuzzy(String centerPointFuzzy)
		{
			this.centerPointFuzzy = centerPointFuzzy;
			return this;
		}

		private String polygonPointsFuzzy;

		public Builder setPolygonPointsFuzzy(String polygonPointsFuzzy)
		{
			this.polygonPointsFuzzy = polygonPointsFuzzy;
			return this;
		}

		private List<String> regionIdList;

		public Builder setRegionIdList(List<String> regionIdList)
		{
			this.regionIdList = regionIdList;
			return this;
		}

		public RegionCenterQuery build()
		{
			return new RegionCenterQuery(this);
		}
	}

	private RegionCenterQuery(Builder builder)
	{
		this.regionId = builder.regionId;
		this.centerPoint = builder.centerPoint;
		this.polygonPoints = builder.polygonPoints;
		this.regionIdFuzzy = builder.regionIdFuzzy;
		this.centerPointFuzzy = builder.centerPointFuzzy;
		this.polygonPointsFuzzy = builder.polygonPointsFuzzy;
		this.regionIdList = builder.regionIdList;

	}
}