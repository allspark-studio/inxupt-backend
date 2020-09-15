package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 陈帅
 * @date 2020/8/14
 */
@Data
@ApiModel("密码登录表单")
public class LoginByPasswordForm {

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("密码")
    private String password;
}
