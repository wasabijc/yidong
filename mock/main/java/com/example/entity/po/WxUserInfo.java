package com.example.entity.po;

import com.alibaba.fastjson2.annotation.JSONField;
import java.io.Serializable;

/**
 * 微信用户信息实体类
 */
public class WxUserInfo implements Serializable
{

    /**
     * 普通用户的标识，对当前开发者账号唯一
     */
    @JSONField(name = "openid")
    private String openid; // 用户的唯一标识

    /**
     * 用户昵称
     */
    @JSONField(name = "nickname")
    private String nickname; // 用户昵称

    /**
     * 普通用户性别，1为男性，2为女性
     */
    @JSONField(name = "sex")
    private int gender; // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知

    /**
     * 用户个人资料填写的省份
     */
    @JSONField(name = "province")
    private String province; // 用户个人资料填写的省份

    /**
     * 普通用户个人资料填写的城市
     */
    @JSONField(name = "city")
    private String city; // 普通用户个人资料填写的城市

    /**
     * 国家，如中国为 CN
     */
    @JSONField(name = "country")
    private String country; // 国家，如中国为 CN

    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
     */
    @JSONField(name = "headimgurl")
    private String avatar; // 用户头像URL

    /**
     * 用户统一标识。针对一个微信开放平台账号下的应用，同一用户的unionid是唯一的。
     */
    @JSONField(name = "unionid")
    private String unionid; // 用户在当前开放平台的唯一标识符

    public String getOpenid()
    {
        return openid;
    }

    public String getNickname()
    {
        return nickname;
    }

    public int getGender()
    {
        return gender;
    }

    public String getProvince()
    {
        return province;
    }

    public String getCity()
    {
        return city;
    }

    public String getCountry()
    {
        return country;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public String getUnionid()
    {
        return unionid;
    }
}
