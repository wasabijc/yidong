package com.example.context;

import com.example.entity.po.ClaimInfo;

/**
 * BaseContext 类用于管理当前线程上下文中的用户相关信息。
 * 使用 ThreadLocal 来存储和检索当前线程的用户相关信息，以确保线程安全。
 */
public class BaseContext
{

    // 定义一个 ThreadLocal 变量，用于存储当前线程的用户ID
    public static ThreadLocal<ClaimInfo> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的用户载荷信息。
     *
     * @param claimInfo 当前用户的载荷信息
     */
    public static void setCurrent(ClaimInfo claimInfo)
    {
        threadLocal.set(claimInfo);
    }

    /**
     * 获取当前线程的用户载荷信息。
     *
     * @return 当前用户的载荷信息
     */
    public static ClaimInfo getCurrent()
    {
        return threadLocal.get();
    }

    /**
     * 移除当前线程的用户载荷信息。
     * 这是一个良好的实践，以避免线程池中线程重用时的内存泄漏问题。
     */
    public static void removeCurrent()
    {
        threadLocal.remove();
    }
}
