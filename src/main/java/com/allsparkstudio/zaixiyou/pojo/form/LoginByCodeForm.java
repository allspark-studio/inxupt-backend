package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AlkaidChen
 * @date 2020/8/14
 */
@Data
@ApiModel("验证码登录表单")
public class LoginByCodeForm {

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("验证码")
    private String code;
}
