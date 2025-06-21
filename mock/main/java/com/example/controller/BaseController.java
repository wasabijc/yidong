package com.example.controller;

import com.example.enums.ResponseCodeEnum;
import com.example.entity.vo.ResponseVO;
import com.example.entity.vo.PaginationResultVO;
import com.example.utils.TransferBeanUtils;

/**
 * 基础控制器类，提供通用的响应方法和分页转换方法。
 *
 * @param <T> 响应数据的类型
 */
public class BaseController<T> {
    // 表示操作成功的状态字符串
    protected final String STATUS_SUCCESS = "success";
    // 表示操作失败的状态字符串
    protected final String STATUS_ERROR = "error";

    /**
     * 返回包含数据的成功响应。
     *
     * @param t 响应数据
     * @return 包含状态、代码、信息和数据的 ResponseVO 对象
     */
    protected ResponseVO<T> SuccessResponse(T t) {
        ResponseVO<T> responseVO = new ResponseVO<T>();
        // 设置响应状态为成功
        responseVO.setStatus(STATUS_SUCCESS);
        // 设置响应代码为成功代码
        responseVO.setCode(ResponseCodeEnum.RESPONSE_SUCCESS.getCode());
        // 设置响应信息为成功信息
        responseVO.setMessage(ResponseCodeEnum.RESPONSE_SUCCESS.getMsg());
        // 设置响应数据
        responseVO.setData(t);
        return responseVO;
    }

    /**
     * 返回不包含数据的成功响应。
     *
     * @return 包含状态、代码和信息的 ResponseVO 对象
     */
    protected ResponseVO<T> SuccessResponse() {
        ResponseVO<T> responseVO = new ResponseVO<T>();
        // 设置响应状态为成功
        responseVO.setStatus(STATUS_SUCCESS);
        // 设置响应代码为成功代码
        responseVO.setCode(ResponseCodeEnum.RESPONSE_SUCCESS.getCode());
        // 设置响应信息为成功信息
        responseVO.setMessage(ResponseCodeEnum.RESPONSE_SUCCESS.getMsg());
        // 不包含数据，设置为 null
        responseVO.setData(null);
        return responseVO;
    }

    /**
     * 将源分页结果对象转换为目标类的分页结果对象。
     *
     * @param sourcePaginationResultVO 源分页结果对象
     * @param targetClassz             目标类的 Class 对象
     * @param <S>                      源对象的类型
     * @param <T>                      目标对象的类型
     * @return 包含目标类对象的 PaginationResultVO 对象
     */
    protected <S, T> PaginationResultVO<T> convert2TargetPaginationVO(PaginationResultVO<S> sourcePaginationResultVO,
                                                                      Class<T> targetClassz) {
        // 创建一个新的目标类的分页结果对象
        PaginationResultVO<T> targetPaginationResultVO = new PaginationResultVO<>();
        // 使用 TransferBeanUtils 工具类将源对象列表转换为目标对象列表
        targetPaginationResultVO.setList(TransferBeanUtils.transferList(sourcePaginationResultVO.getList(), targetClassz));
        // 设置分页的其他属性
        targetPaginationResultVO.setPageIndex(sourcePaginationResultVO.getPageIndex());
        targetPaginationResultVO.setEntryNum(sourcePaginationResultVO.getEntryNum());
        targetPaginationResultVO.setPageSize(sourcePaginationResultVO.getPageSize());
        targetPaginationResultVO.setPageTotal(sourcePaginationResultVO.getPageTotal());
        return targetPaginationResultVO;
    }
}
