package com.example.entity.po;

import com.alibaba.fastjson2.annotation.JSONField;
import java.io.Serializable;

/**
 * QQ用户信息实体类
 */
public class QqUserInfo implements Serializable
{
    /**
     * 返回码
     */
    @JSONField(name = "ret")
    private String returnCode;

    /**
     * 错误信息提示，当 ret < 0 时存在。返回数据全部使用 UTF-8 编码。
     */
    @JSONField(name = "msg")
    private String errMessage;

    /**
     * 用户在 QQ 空间的昵称
     */
    @JSONField(name = "nickname")
    private String nickname;

    /**
     * 大小为 40×40 像素的 QQ 头像 URL
     */
    @JSONField(name = "figureurl_qq_1")
    private String avatar40;

    /**
     * 大小为 100×100 像素的 QQ 头像 URL
     * 注意：并非所有用户都有 100×100 像素的头像，但所有用户都有 40×40 像素的头像。
     */
    @JSONField(name = "figureurl_qq_2")
    private String avatar100;

    /**
     * 性别。如果获取不到则默认返回 "男"
     */
    @JSONField(name = "gender")
    private String gender;

    public String getReturnCode()
    {
        return returnCode;
    }

    public String getErrMessage()
    {
        return errMessage;
    }

    public String getNickname()
    {
        return nickname;
    }

    public String getAvatar40()
    {
        return avatar40;
    }

    public String getAvatar100()
    {
        return avatar100;
    }

    public String getGender()
    {
        return gender;
    }
}
