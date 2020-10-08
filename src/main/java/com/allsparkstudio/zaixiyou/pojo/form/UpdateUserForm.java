package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("更新用户信息表单")
public class UpdateUserForm {

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("个性签名")
    private String description;

    @ApiModelProperty("性别 0保密 1男 2女")
    private Integer gender;

    @ApiModelProperty("年级")
    private String grade;

    @ApiModelProperty("专业")
    private String major;

}
