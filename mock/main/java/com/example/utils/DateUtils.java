package com.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class DateUtils
{
    // 日期格式常量
    public static final String PATTERN_DATE_ONLY = "yyyy-MM-dd";
    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_SLASH_DATE = "yyyy/MM/dd";
    public static final String PATTERN_YEAR_MONTH_DAY = "yyyy年MM月dd日";

    // 使用 ConcurrentHashMap 确保线程安全地访问 SimpleDateFormat 实例
    private static final ConcurrentHashMap<String, ThreadLocal<SimpleDateFormat>> formatterMap = new ConcurrentHashMap<>();

    /**
     * 根据给定的模式获取一个线程局部的 SimpleDateFormat 实例。
     *
     * @param pattern 日期格式模式。
     * @return SimpleDateFormat 实例。
     */
    private static SimpleDateFormat getDateFormat(String pattern)
    {
        return formatterMap.computeIfAbsent(pattern, k -> ThreadLocal.withInitial(() -> new SimpleDateFormat(k))).get();
    }

    /**
     * 按指定格式将给定日期格式化为字符串。
     *
     * @param date    要格式化的日期。
     * @param pattern 用于格式化的模式。
     * @return 格式化后的日期字符串。
     */
    public static String format(Date date, String pattern)
    {
        return getDateFormat(pattern).format(date);
    }

    /**
     * 按照指定模式解析日期字符串为 Date 对象。
     *
     * @param dateStr 要解析的日期字符串。
     * @param pattern 日期字符串中的模式。
     * @return 解析得到的 Date 对象。
     * @throws ParseException 如果日期字符串与模式不匹配。
     */
    public static Date parse(String dateStr, String pattern) throws ParseException
    {
        try
        {
            return getDateFormat(pattern).parse(dateStr);
        }
        catch (ParseException e)
        {
            // 重新抛出异常，提供更详细的错误信息
            throw new ParseException("Error parsing date: " + dateStr + " with pattern: " + pattern, e.getErrorOffset());
        }
    }
}
