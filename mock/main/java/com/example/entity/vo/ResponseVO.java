package com.example.entity.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 响应值对象类，用于封装响应数据
 *
 * @param <T> 响应数据的类型
 */
public class ResponseVO<T> implements Serializable {
    private String status;
    private Integer code;
    private String message;
    private T data;

    /**
     * 成功响应，不包含数据
     *
     * @param <T> 响应数据的类型
     * @return 响应对象
     */
    public static <T> ResponseVO<T> success() {
        ResponseVO<T> result = new ResponseVO<T>();
        result.code = 1;
        return result;
    }

    /**
     * 成功响应，包含数据
     *
     * @param <T>    响应数据的类型
     * @param object 响应数据
     * @return 响应对象
     */
    public static <T> ResponseVO<T> success(T object) {
        ResponseVO<T> result = new ResponseVO<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    /**
     * 错误响应，包含错误信息
     *
     * @param msg 错误信息
     * @param <T> 响应数据的类型
     * @return 响应对象
     */
    public static <T> ResponseVO<T> error(String msg) {
        ResponseVO<T> result = new ResponseVO<T>();
        result.message = msg;
        result.code = 0;
        return result;
    }

    /**
     * 获取状态
     *
     * @return 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取响应代码
     *
     * @return 响应代码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置响应代码
     *
     * @param code 响应代码
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取信息
     *
     * @return 信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置信息
     *
     * @param message 信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取数据
     *
     * @return 数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    public void setData(T data) {
        this.data = data;
    }
}


