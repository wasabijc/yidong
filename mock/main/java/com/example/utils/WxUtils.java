package com.example.utils;

import com.example.entity.po.WxUserInfo;
import com.example.exception.BusinessException;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.util.Json;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Component
public class WxUtils
{
    @Value("${wechat.appid}")
    private String appId;

    @Value("${wechat.secret}")
    private String secretKey;

    @Value("${wechat.redirect-url}")
    private String redirectUrl;

    public static final String authorizeUrl = "https://open.weixin.qq.com/connect/qrconnect";

    public static final String openIdAndAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";

    public static final String refreshTokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    public static final String checkTokenValidationUrl = "https://api.weixin.qq.com/sns/auth";

    public static final String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";

    public static final String SESSION_KEY_WX_REFRESH_TOKEN = "wx_refresh_token";

    public static final String SESSION_KEY_WX_UNION_ID = "wx_unionid";

    public static final int WX_NICKNAME_MAX_LENGTH = 20;

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
        map.put("scope", "snsapi_login");
        map.put("appid", appId);
        map.put("redirect_uri", redirectUrl);
        map.put("state", state);

        String url = HttpUtils.buildUrlWithQueryParams(authorizeUrl, map);
        return url;
    }

    /**
     * 根据授权码获取用户的openid、access_token和unionid。
     *
     * @param code 授权码
     * @return 包含openid、access_token和unionid的字符串数组
     * @throws IOException       如果网络请求失败
     * @throws BusinessException 如果获取过程中发生业务异常，如openid或access_token丢失
     */
    public String[] getOpenIdAndAccessTokenAndUnionId(String code) throws IOException, BusinessException
    {
        // 构建请求参数
        HashMap<String, String> map = new HashMap<>();
        map.put("appid", appId);
        map.put("secret", secretKey);
        map.put("code", code);
        map.put("grant_type", "authorization_code");

        // 调用微信接口服务，获取JSON格式的响应数据
        String json = HttpUtils.doGet(openIdAndAccessTokenUrl, map);

        // 解析JSON数据为JSONObject对象
        JSONObject jsonObject = JsonUtils.convertJson2Obj(json);

        // 提取必要的字段信息
        String openid = jsonObject.getString("openid");
        String access_token = jsonObject.getString("access_token");
        String unionid = jsonObject.getString("unionid");
        String refresh_token = jsonObject.getString("refresh_token");

        // 检查必要字段是否为空，如果为空则抛出业务异常
        if (openid == null || openid.isEmpty())
        {
            throw new BusinessException("wechat log in fail: openid lost");
        }
        else if (access_token == null || access_token.isEmpty())
        {
            throw new BusinessException("wechat log in fail: access_token lost");
        }

        // 获取当前会话的HttpSession对象，并将refresh_token存入会话中
        HttpSession session = ServletUtils.getHttpSession();
        session.setAttribute(SESSION_KEY_WX_REFRESH_TOKEN, refresh_token);

        // 返回包含openid、access_token和unionid的字符串数组
        String[] openIdAndAccessToken = {openid, access_token, unionid};
        return openIdAndAccessToken;
    }

    /**
     * 刷新access_token。
     *
     * @return 刷新后的access_token
     * @throws IOException       如果网络请求失败
     * @throws BusinessException 如果refresh_token不存在或为空
     */
    public String refreshToken() throws IOException, BusinessException
    {
        // 获取当前会话的HttpSession对象，并从会话中获取refresh_token
        HttpSession session = ServletUtils.getHttpSession();
        String refreshToken = (String) session.getAttribute(SESSION_KEY_WX_REFRESH_TOKEN);

        // 如果refresh_token不存在或为空，抛出业务异常
        if (refreshToken == null || refreshToken.isEmpty())
        {
            throw new BusinessException("refresh token for wechat does not exist.");
        }

        // 构建请求参数
        HashMap<String, String> map = new HashMap<>();
        map.put("appid", appId);
        map.put("refresh_code", refreshToken);
        map.put("grant_type", "refresh_code");

        // 调用微信接口服务，发送GET请求，获取JSON格式的响应数据
        String json = HttpUtils.doGet(refreshTokenUrl, map);

        // 解析JSON数据为JSONObject对象
        JSONObject jsonObject = JsonUtils.convertJson2Obj(json);

        // 提取新的access_token和refresh_token，并更新会话中的refresh_token
        String access_token = jsonObject.getString("access_token");
        String newRefreshToken = jsonObject.getString("refresh_token");
        session.setAttribute(SESSION_KEY_WX_REFRESH_TOKEN, newRefreshToken);

        // 返回刷新后的access_token
        return access_token;
    }


    /**
     * 检查access_token的有效性。
     *
     * @param accessToken 待检查的access_token
     * @param openid      用户的openid
     * @return access_token是否有效
     * @throws IOException 如果网络请求失败
     */
    public boolean checkAccessTokenValidation(String accessToken, String openid) throws IOException
    {
        // 构建请求参数
        HashMap<String, String> map = new HashMap<>();
        map.put("openid", openid);
        map.put("access_token", accessToken);

        // 调用微信接口服务，获取JSON格式的响应数据
        String json = HttpUtils.doGet(checkTokenValidationUrl, map);

        // 解析JSON数据为JSONObject对象
        JSONObject jsonObject = JsonUtils.convertJson2Obj(json);

        // 提取errMsg字段，判断access_token是否有效
        String errMsg = jsonObject.getString("errMsg");
        return errMsg.equals("ok");
    }

    /**
     * 根据access_token和openid获取用户的微信用户信息。
     *
     * @param accessToken 微信接口调用凭证
     * @param openid      用户的openid
     * @return 微信用户信息对象
     * @throws IOException 如果网络请求失败
     */
    public WxUserInfo getWxUserInfo(String accessToken, String openid) throws IOException
    {
        // 构建请求参数
        HashMap<String, String> map = new HashMap<>();
        map.put("openid", openid);
        map.put("access_token", accessToken);
        map.put("lang", "zh_CN");

        // 调用微信接口服务，获取JSON格式的用户信息数据
        String json = HttpUtils.doGet(userInfoUrl, map);

        // 将JSON数据转换为WxUserInfo对象
        WxUserInfo wxUserInfo = JsonUtils.convertJson2Obj(json, WxUserInfo.class);

        // 返回微信用户信息对象
        return wxUserInfo;
    }

    public String getAppId()
    {
        return appId;
    }

    public String getSecretKey()
    {
        return secretKey;
    }

    public String getRedirectUrl()
    {
        return redirectUrl;
    }
}
