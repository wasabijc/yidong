package com.example.utils;

import com.alibaba.fastjson2.JSON;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HttpUtils 是一个基于 OkHttp3 的 HTTP 请求工具类。
 * 提供了常见的同步和异步的 GET 和 POST 请求方法。
 */
public class HttpUtils
{
    private static final OkHttpClient client;

    static
    {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 将 URL 与查询参数连接起来。
     *
     * @param baseUrl     基础 URL
     * @param queryParams 查询参数的 Map 集合
     * @return 拼接后的包含查询参数的 URL 字符串
     * @throws IllegalArgumentException 如果 baseUrl 解析失败抛出 IllegalArgumentException
     */
    public static String buildUrlWithQueryParams(String baseUrl, Map<String, String> queryParams)
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();

        if (queryParams != null)
        {
            for(Map.Entry<String, String> entry: queryParams.entrySet())
            {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        return urlBuilder.build().toString();
    }


    /**
     * 发送同步 GET 请求。
     *
     * @param url 请求的 URL
     * @return 响应的字符串
     * @throws IOException 如果请求失败抛出 IOException
     */
    public static String doGet(String url) throws IOException
    {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute())
        {
            if (!response.isSuccessful())
            {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    /**
     * 发送异步 GET 请求。
     *
     * @param url      请求的 URL
     * @param callback 回调接口，用于处理响应或错误
     */
    public static void doGetAsync(String url, Callback callback)
    {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 发送同步 GET 请求。
     *
     * @param url      请求的 URL
     * @param paramMap 请求参数的 Map 集合
     * @return 响应的字符串
     * @throws IOException 如果请求失败抛出 IOException
     */
    public static String doGet(String url, Map<String, String> paramMap) throws IOException
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (paramMap != null)
        {
            for(Map.Entry<String, String> entry: paramMap.entrySet())
            {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        try (Response response = client.newCall(request).execute())
        {
            if (!response.isSuccessful())
            {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    /**
     * 发送异步 GET 请求。
     *
     * @param url      请求的 URL
     * @param paramMap 请求参数的 Map 集合
     * @param callback 回调接口，用于处理响应或错误
     */
    public static void doGetAsync(String url, Map<String, String> paramMap, Callback callback)
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (paramMap != null)
        {
            for(Map.Entry<String, String> entry: paramMap.entrySet())
            {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 发送同步 POST 请求，发送 JSON 数据。
     *
     * @param url     请求的 URL
     * @param jsonMap 发送的 JSON 数据的 Map 集合
     * @return 响应的字符串
     * @throws IOException 如果请求失败抛出 IOException
     */
    public static String doPost(String url, Map<String, Object> jsonMap) throws IOException
    {
        String json = JSON.toJSONString(jsonMap);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute())
        {
            if (!response.isSuccessful())
            {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    /**
     * 发送异步 POST 请求，发送 JSON 数据。
     *
     * @param url      请求的 URL
     * @param jsonMap  发送的 JSON 数据的 Map 集合
     * @param callback 回调接口，用于处理响应或错误
     */
    public static void doPostAsync(String url, Map<String, Object> jsonMap, Callback callback)
    {
        String json = JSON.toJSONString(jsonMap);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 发送同步 POST 请求，发送表单数据。
     *
     * @param url      请求的 URL
     * @param formData 表单数据的 Map 集合
     * @return 响应的字符串
     * @throws IOException 如果请求失败抛出 IOException
     */
    public static String doPostForm(String url, Map<String, String> formData) throws IOException
    {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for(Map.Entry<String, String> entry: formData.entrySet())
        {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody body = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute())
        {
            if (!response.isSuccessful())
            {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    /**
     * 发送异步 POST 请求，发送表单数据。
     *
     * @param url      请求的 URL
     * @param formData 表单数据的 Map 集合
     * @param callback 回调接口，用于处理响应或错误
     */
    public static void doPostFormAsync(String url, Map<String, String> formData, Callback callback)
    {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for(Map.Entry<String, String> entry: formData.entrySet())
        {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody body = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
