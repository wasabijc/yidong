package com.example.entity.query;

import java.util.List;;


/**
 * @description 查询对象
 * @since 2025-06-21
 * @author LingoJack
 */
public class UserInfoQuery extends BaseQuery
{
	/**
	 * 用户唯一标识
	 */
	private String imsi;

	/**
	 * 性别(0:女,1:男)
	 */
	private Integer gender;

	/**
	 * 年龄
	 */
	private Integer age;

	/**
	 * 支持模糊查询的用户唯一标识
	 */
	private String imsiFuzzy;

	/**
	 * 批量查询列表: 用户唯一标识
	 */
	private List<String> imsiList;

	public UserInfoQuery()
	{

	}

	public UserInfoQuery(String imsi, Integer gender, Integer age)
	{
		this.imsi = imsi;
		this.gender = gender;
		this.age = age;
	}

	public void setImsi(String imsi)
	{
		this.imsi = imsi;
	}

	public String getImsi()
	{
		return this.imsi;
	}

	public void setGender(Integer gender)
	{
		this.gender = gender;
	}

	public Integer getGender()
	{
		return this.gender;
	}

	public void setAge(Integer age)
	{
		this.age = age;
	}

	public Integer getAge()
	{
		return this.age;
	}

	public void setImsiFuzzy(String imsiFuzzy)
	{
		this.imsiFuzzy = imsiFuzzy;
	}

	public String getImsiFuzzy()
	{
		return this.imsiFuzzy;
	}

	public void setImsiList(List<String> imsiList)
	{
		this.imsiList = imsiList;
	}

	public List<String> getImsiList()
	{
		return this.imsiList;
	}

	public static Builder builder()
	{
		 return new Builder();
	}

	private static class Builder
	{
		private String imsi;

		public Builder setImsi(String imsi)
		{
			this.imsi = imsi;
			return this;
		}

		private Integer gender;

		public Builder setGender(Integer gender)
		{
			this.gender = gender;
			return this;
		}

		private Integer age;

		public Builder setAge(Integer age)
		{
			this.age = age;
			return this;
		}

		private String imsiFuzzy;

		public Builder setImsiFuzzy(String imsiFuzzy)
		{
			this.imsiFuzzy = imsiFuzzy;
			return this;
		}

		private List<String> imsiList;

		public Builder setImsiList(List<String> imsiList)
		{
			this.imsiList = imsiList;
			return this;
		}

		public UserInfoQuery build()
		{
			return new UserInfoQuery(this);
		}
	}

	private UserInfoQuery(Builder builder)
	{
		this.imsi = builder.imsi;
		this.gender = builder.gender;
		this.age = builder.age;
		this.imsiFuzzy = builder.imsiFuzzy;
		this.imsiList = builder.imsiList;

	}
}