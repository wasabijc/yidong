package com.example.utils;

import com.alibaba.fastjson2.JSON; // 导入FastJSON2库
import com.alibaba.fastjson2.JSONObject;

import java.util.List;

/**
 * JSON工具类，使用FastJSON2库进行JSON处理。
 */
public class JsonUtils
{

    /**
     * 将对象转换为JSON字符串。
     *
     * @param object 待转换的对象
     * @return 转换后的JSON字符串
     */
    public static String convertObj2Json(Object object)
    {
        return JSON.toJSONString(object);
    }

    /**
     * 将JSON字符串转换为JSONObject类型的对象。
     *
     * @param json 待转换的JSON字符串
     * @return 转换后的对象
     */
    public static JSONObject convertJson2Obj(String json)
    {
        return JSON.parseObject(json);
    }

    /**
     * 将JSON字符串转换为指定类型的对象。
     *
     * @param json   待转换的JSON字符串
     * @param classz 目标对象的Class类型
     * @param <T>    目标对象的泛型类型
     * @return 转换后的对象
     */
    public static <T> T convertJson2Obj(String json, Class<T> classz)
    {
        return JSON.parseObject(json, classz);
    }

    /**
     * 将JSON数组字符串转换为指定类型的对象列表。
     *
     * @param json   JSON数组字符串
     * @param classz 目标对象的Class类型
     * @param <T>    目标对象的泛型类型
     * @return 转换后的对象列表
     */
    public static <T> List<T> convertJsonArray2List(String json, Class<T> classz)
    {
        return JSON.parseArray(json, classz);
    }
}
