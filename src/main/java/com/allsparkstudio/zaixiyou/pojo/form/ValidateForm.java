package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 陈帅
 * @date 2020/7/23
 */
@Data
@ApiModel("验证手机号表单")
public class ValidateForm {

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("验证码")
    private String code;

}
