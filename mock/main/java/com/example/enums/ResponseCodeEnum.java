package com.example.enums;

public enum ResponseCodeEnum
{
    RESPONSE_SUCCESS(200, "response successfully"),
    PAGE_NOT_FOUND(404, "page not found"),
    REQUEST_PARAMS_ERROR(600, "request parameter error"),
    INFORMATION_EXISTED(601, "information has existed"),
    LOGIN_FIRST(401, "please log in first"),
    LOGIN_TIMEOUT(901, "login timeout, please log in again"),
    SERVER_ERROR(502, "server error"),
    FORBIDDEN(403, "access denied");

    private Integer code;
    private String msg;

    ResponseCodeEnum(Integer code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }
}
