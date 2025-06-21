package com.example.entity.po;

import java.io.Serializable;


/**
 * @description 
 * @since 2025-06-21
 * @author LingoJack
 */
public class UserInfoEntity implements Serializable
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

	public UserInfoEntity()
	{

	}

	public UserInfoEntity(String imsi, Integer gender, Integer age)
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

	@Override
	public String toString()
	{
		return " 用户唯一标识:" + (this.imsi == null ? "no value" : this.imsi) + " 性别(0:女,1:男):" + (this.gender == null ? "no value" : this.gender) + " 年龄:" + (this.age == null ? "no value" : this.age) ;
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

		public UserInfoEntity build()
		{
			return new UserInfoEntity(this);
		}
	}

	private UserInfoEntity(Builder builder)
	{
		this.imsi = builder.imsi;
		this.gender = builder.gender;
		this.age = builder.age;

	}
}