package com.example.utils;

import com.example.entity.po.QqUserInfo;
import com.example.exception.BusinessException;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * QQ相关工具类，包括获取用户授权URL、获取access_token、获取openid以及获取QQ用户信息等功能。
 */
@Component
public class QqUtils
{

    /**
     * QQ授权页面URL
     */
    public static final String authorizeUrl = "https://graph.qq.com/oauth2.0/authorize";

    /**
     * 获取access_token的URL
     */
    public static final String tokenUrl = "https://graph.qq.com/oauth2.0/token";

    /**
     * 获取openid的URL
     */
    public static final String openIdUrl = "https://graph.qq.com/oauth2.0/me";

    /**
     * 获取QQ用户信息的URL
     */
    public static final String userInfoUrl = "https://graph.qq.com/user/get_user_info";

    @Value("${qq.redirect-url}")
    private String redirectUrl; // QQ授权回调URL，从配置中读取

    @Value("${qq.app-id}")
    private String appId; // QQ应用ID，从配置中读取

    @Value("${qq.app-key}")
    private String appKey; // QQ应用Key，从配置中读取

    public static final int QQ_NICKNAME_MAX_LENGTH = 20;

    /**
     * 获取询问用户是否授权页面的URL。
     *
     * @param state 随机状态码，用于防止CSRF攻击
     * @return 用户授权URL
     */
    public String getUserAuthorizeUrl(String state)
    {
        redirectUrl = URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);

        HashMap<String, String> map = new HashMap<>();
        map.put("response_type", "code");
        map.put("client_id", appId);
        map.put("redirect_uri", redirectUrl);
        map.put("state", state);

        String url = HttpUtils.buildUrlWithQueryParams(authorizeUrl, map);
        return url;
    }

    /**
     * 根据授权码获取access_token。
     *
     * @param code 授权码
     * @return access_token
     * @throws BusinessException 如果获取过程中发生业务异常
     * @throws IOException       如果IO操作失败
     */
    public String getAccessToken(String code) throws BusinessException, IOException
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("grant_type", "authorization_code");
        map.put("client_id", appId);
        map.put("client_secret", appKey);
        map.put("code", code);
        map.put("redirect_uri", redirectUrl);

        String json = HttpUtils.doGet(tokenUrl, map);
        JSONObject jsonObject = JsonUtils.convertJson2Obj(json);
        String access_token = jsonObject.getString("access_token");
        if (access_token == null)
        {
            throw new BusinessException("QQ log in fail: access_token lost");
        }
        return access_token;
    }

    /**
     * 根据access_token获取openid。
     *
     * @param accessToken access_token
     * @return openid
     * @throws BusinessException 如果获取过程中发生业务异常
     * @throws IOException       如果IO操作失败
     */
    public String getOpenId(String accessToken) throws BusinessException, IOException
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);

        String json = HttpUtils.doGet(openIdUrl, map);
        JSONObject jsonObject = JsonUtils.convertJson2Obj(json);
        String openid = jsonObject.getString("openid");
        if (openid == null)
        {
            throw new BusinessException("QQ log in fail: openid lost");
        }
        return openid;
    }

    /**
     * 根据access_token和openid获取QQ用户信息。
     *
     * @param accessToken access_token
     * @param openid      openid
     * @return QQ用户信息
     * @throws IOException 如果IO操作失败
     */
    public QqUserInfo getQqUserInfo(String accessToken, String openid) throws IOException
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("oauth_consumer_key", appId);
        map.put("openid", openid);

        String json = HttpUtils.doGet(userInfoUrl, map);
        QqUserInfo qqUserInfo = JsonUtils.convertJson2Obj(json, QqUserInfo.class);
        return qqUserInfo;
    }

    /**
     * 获取QQ授权回调URL。
     *
     * @return QQ授权回调URL
     */
    public String getRedirectUrl()
    {
        return redirectUrl;
    }

    /**
     * 获取QQ应用ID。
     *
     * @return QQ应用ID
     */
    public String getAppId()
    {
        return appId;
    }

    /**
     * 获取QQ应用Key。
     *
     * @return QQ应用Key
     */
    public String getAppKey()
    {
        return appKey;
    }
}
