package com.allsparkstudio.zaixiyou.pojo.log;

import lombok.Data;

import java.util.Date;

/**
 * @author AlkaidChen
 * @date 2021/5/23
 */
@Data
public class ApiLog {
    /**
     * 操作描述
     */
    private String description;

    /**
     * 操作时间
     */
    private Date startTime;

    /**
     * 消耗时间
     */
    private Integer spendTime;

    /**
     * URL
     */
    private String url;

    /**
     * 请求类型
     */
    private String method;

    /**
     * IP地址
     */
    private String ip;

    /**
     * Token
     */
    private String token;

    /**
     * 请求参数
     */
    private Object parameter;

    /**
     * 请求返回的结果
     */
    private Object result;
}
