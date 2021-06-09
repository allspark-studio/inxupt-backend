package com.allsparkstudio.zaixiyou.pojo.vo;

import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author AlkaidChen
 * @date 2020/7/17
 * 基本VO对象
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseVO<T> {

    private Integer status;

    private String msg;

    private T data;

    private ResponseVO(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ResponseVO(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseVO<T> success(Integer status, String msg, T data) {
        return new ResponseVO<T>(ResponseEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> ResponseVO<T> success(T data, String msg) {
        return new ResponseVO<T>(ResponseEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> ResponseVO<T> success(Integer status, String msg) {
        return new ResponseVO<>(status, msg);
    }

    public static <T> ResponseVO<T> success(String msg) {
        return new ResponseVO<>(ResponseEnum.SUCCESS.getCode(), msg);
    }

    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<T>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), data);
    }

    public static <T> ResponseVO<T> success() {
        return new ResponseVO<T>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg());
    }

    //这里new ResponseVO<T> 里面需要把泛型加上，不然报错. 不知道为啥
    public static <T> ResponseVO<T> error(Integer status, String msg) {
        return new ResponseVO<T>(status, msg);
    }

    public static <T> ResponseVO<T> error(ResponseEnum responseEnum) {
        return error(responseEnum.getCode(), responseEnum.getMsg());
    }

    public static <T> ResponseVO<T> error(ResponseEnum responseEnum, String msg) {
        return error(responseEnum.getCode(), msg);
    }
}
