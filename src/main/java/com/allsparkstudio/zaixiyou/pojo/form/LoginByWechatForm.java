package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AlkaidChen
 * @date 2023/5/7
 */
@Data
@ApiModel("微信登录表单")
public class LoginByWechatForm {

    @ApiModelProperty("微信临时登录凭证")
    private String code;
}
