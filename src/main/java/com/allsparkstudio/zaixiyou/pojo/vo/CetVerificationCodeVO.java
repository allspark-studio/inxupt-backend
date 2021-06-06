package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

/**
 * @Author: Marble
 * @Date: 2021/6/3 22:08
 * @Description: 获取cet验证码，同时会返回个cookie信息
 */
@Data
public class CetVerificationCodeVO  {
    /**
     * 图片 格式为Base64
     */
    private String image;
    /**
     * cookie信息  每次获取新的图片时都会产生
     */
    private String cookie;
}
