package com.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类，用于在不同类型的 JavaBean 之间传递属性。
 * 注意Bean要求
 */
public class TransferBeanUtils
{
    private static Logger logger = LoggerFactory.getLogger(TransferBeanUtils.class);

    /**
     * 将源对象的属性传递到目标类的对象中。
     *
     * @param sourceObj    源对象，属性将从该对象复制
     * @param targetClassz 目标类的 Class 对象
     * @param <S>          源对象的类型
     * @param <T>          目标对象的类型
     * @return 一个新的目标类的实例，属性已经从源对象复制过来
     */
    public static <S, T> T transferObj(S sourceObj, Class<T> targetClassz)
    {
        T targetObj = null;
        try
        {
            // 创建目标类的一个新实例
            targetObj = targetClassz.getDeclaredConstructor().newInstance();
        }
        catch (Exception e)
        {
            logger.error("error when transferring object",e);
        }
        // 复制源对象的属性到目标对象
        BeanUtils.copyProperties(sourceObj, targetObj);
        return targetObj;
    }

    /**
     * 将源对象列表的属性传递到目标类对象列表中。
     *
     * @param sourceList   源对象列表，属性将从该列表中的每个对象复制
     * @param targetClassz 目标类的 Class 对象
     * @param <S>          源对象的类型
     * @param <T>          目标对象的类型
     * @return 一个新的目标类的实例列表，属性已经从源对象列表中的每个对象复制过来
     */
    public static <S, T> List<T> transferList(List<S> sourceList, Class<T> targetClassz)
    {
        List<T> targetList = new ArrayList<T>();
        for(S s: sourceList)
        {
            // 将每个源对象的属性复制到目标对象，并添加到目标列表中
            T targetObj = transferObj(s, targetClassz);
            targetList.add(targetObj);
        }
        return targetList;
    }
}
