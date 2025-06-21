package com.example.exception;

import com.example.enums.ResponseCodeEnum;

public class BusinessException extends Exception
{
    private ResponseCodeEnum codeEnum;
    private Integer code;
    private String message;

    public BusinessException(String message, Throwable cause)
    {
        super(message, cause);
        this.message = message;
    }

    public BusinessException(String message)
    {
        super(message);
        this.message = message;
    }

    public BusinessException(Throwable cause)
    {
        super(cause);
    }

    public BusinessException(ResponseCodeEnum codeEnum)
    {
        super(codeEnum.getMsg());
        this.code=codeEnum.getCode();
        this.message=codeEnum.getMsg();
        this.codeEnum = codeEnum;
    }

    public BusinessException(String message, Integer code)
    {
        super(message);
        this.code = code;
        this.message=message;
    }

    public ResponseCodeEnum getCodeEnum()
    {
        return codeEnum;
    }

    public Integer getCode()
    {
        return code;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    /**
     * 业务异常无需堆栈信息，提高效率
     * @return
     */
    @Override
    public Throwable fillInStackTrace()
    {
        return this;
    }
}
