package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 陈帅
 * @date 2020/8/25
 */

@Data
@ApiModel("设置密码表单")
public class PasswordForm {

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("再次输入密码")
    private String checkPassword;
}
