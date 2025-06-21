package com.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PropertiesUtils 工具类用于加载和读取属性文件中的键值对。
 * 该类在静态代码块中加载配置文件，并将属性存储在一个线程安全的 Map 中，
 * 以便在整个应用程序的生命周期内快速检索。
 */
public class PropertiesUtils
{
    // 存储属性的对象
    private static Properties props = new Properties();
    // 用于缓存属性的线程安全的 Map
    private static Map<String, String> propertiesMap = new ConcurrentHashMap<>();
    // 配置文件名称
    private static final String propertyFile = "application.properties";

    // 静态代码块在类加载时执行一次
    static
    {
        InputStream is = null;
        try
        {
            // 加载属性文件 application.properties
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream(propertyFile);
            props.load(is);

            // 将属性文件中的键值对存储到 PROPERTIES_MAP 中
            Iterator<Object> iterator = props.keySet().iterator();
            while (iterator.hasNext())
            {
                String key = (String) iterator.next();
                propertiesMap.put(key, props.getProperty(key));
            }
        }
        catch (IOException e)
        {
            // 如果发生 I/O 异常，抛出运行时异常
            throw new RuntimeException(e);
        }
        finally
        {
            // 确保 InputStream 在使用后关闭
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    // 如果关闭 InputStream 时发生 I/O 异常，抛出运行时异常
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 根据键获取属性值
     *
     * @param key 属性的键
     * @return 属性的值
     */
    public static String getProperty(String key)
    {
        return propertiesMap.get(key);
    }
}
