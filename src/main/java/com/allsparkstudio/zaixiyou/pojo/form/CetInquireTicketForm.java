package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : Marble
 * @Date : 2021/6/2 17:04
 * @Description : 找回准考证号需要的信息
 */
@Data
@ApiModel("找回准考证号需要的信息")
public class CetInquireTicketForm {
    @ApiModelProperty("省份码")
    private String provinceCode;
    @ApiModelProperty("身份证类型码")
    private String idTypeCode;
    @ApiModelProperty("省份证码")
    private String idNumber;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("验证码")
    private String verificationCode;
    @ApiModelProperty("Cookie信息")
    private String cookie;
}
