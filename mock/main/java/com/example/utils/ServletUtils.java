package com.example.utils;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 一个用于从当前请求上下文中获取与 servlet 相关的对象（如 ServletRequest、ServletResponse 和 HttpSession）的工具类。
 */
public class ServletUtils
{

    /**
     * 从当前请求上下文中获取 ServletRequest 对象。
     *
     * @return 当前的 ServletRequest 对象，如果当前请求上下文不可用，则返回 null。
     */
    public static ServletRequest getServletRequest()
    {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes != null ? servletRequestAttributes.getRequest() : null;
    }

    /**
     * 从当前请求上下文中获取 HttpSession 对象。
     *
     * @return 当前的 HttpSession 对象，如果当前请求上下文不可用，则返回 null。
     */
    public static HttpSession getHttpSession()
    {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes != null ? servletRequestAttributes.getRequest().getSession() : null;
    }

    /**
     * 从当前请求上下文中获取 ServletResponse 对象。
     *
     * @return 当前的 ServletResponse 对象，如果当前请求上下文不可用，则返回 null。
     */
    public static ServletResponse getServletResponse()
    {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes != null ? servletRequestAttributes.getResponse() : null;
    }

    /**
     * 从当前请求上下文中获取 HttpServletRequest 对象。
     *
     * @return 当前的 HttpServletRequest 对象，如果当前请求上下文不可用，则返回 null。
     */
    public static HttpServletRequest getHttpServletRequest()
    {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes != null ? servletRequestAttributes.getRequest() : null;
    }

    /**
     * 从当前请求中获取客户端的 IP 地址。
     *
     * @return 客户端的 IP 地址，如果无法获取，则返回 null。
     */
    public static String getClientIpAddress()
    {
        ServletRequest request = getServletRequest();
        if (request != null)
        {
            String ip = request.getRemoteAddr();
            if (request instanceof HttpServletRequest httpServletRequest)
            {
                String xForwardedForHeader = httpServletRequest.getHeader("X-Forwarded-For");
                if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty())
                {
                    ip = xForwardedForHeader.split(",")[0];
                }
            }
            return ip;
        }
        return null;
    }
}
