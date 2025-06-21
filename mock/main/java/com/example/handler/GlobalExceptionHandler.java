package com.example.handler;

import com.example.enums.ResponseCodeEnum;
import com.example.entity.vo.ResponseVO;
import com.example.controller.BaseController;
import com.example.exception.BusinessException;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    Object handleException(Exception e, HttpServletRequest request) {
        logger.error("request error, url:{}, error info:{}", request.getRequestURL(), e.getMessage());
        ResponseVO responseVO = new ResponseVO();

        if (e instanceof NoHandlerFoundException) {
            responseVO.setCode(ResponseCodeEnum.PAGE_NOT_FOUND.getCode());
            responseVO.setMessage(ResponseCodeEnum.PAGE_NOT_FOUND.getMsg());
            responseVO.setStatus(STATUS_ERROR);
        }
        else if (e instanceof BusinessException) {
            // 业务错误
            BusinessException businessException = (BusinessException) e;
            responseVO.setCode(businessException.getCode());
            responseVO.setMessage(businessException.getMessage());
            responseVO.setStatus(STATUS_ERROR);
        }
        else if (e instanceof BindException) {
            // 参数类型错误
            responseVO.setCode(ResponseCodeEnum.REQUEST_PARAMS_ERROR.getCode());
            responseVO.setMessage(ResponseCodeEnum.REQUEST_PARAMS_ERROR.getMsg());
            responseVO.setStatus(STATUS_ERROR);
        }
        else if (e instanceof DuplicateKeyException) {
            // 主键冲突
            responseVO.setCode(ResponseCodeEnum.INFORMATION_EXISTED.getCode());
            responseVO.setMessage(ResponseCodeEnum.INFORMATION_EXISTED.getMsg());
            responseVO.setStatus(STATUS_ERROR);
        }
        else {
            responseVO.setCode(ResponseCodeEnum.SERVER_ERROR.getCode());
            responseVO.setMessage(ResponseCodeEnum.SERVER_ERROR.getMsg());
            responseVO.setStatus(STATUS_ERROR);
        }

        return responseVO;
    }

}
