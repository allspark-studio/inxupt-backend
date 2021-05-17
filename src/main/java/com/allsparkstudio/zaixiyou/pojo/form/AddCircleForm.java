package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AlkaidChen
 * @date 2020/8/24
 */
@Data
@ApiModel("新建圈子")
public class AddCircleForm {

    @ApiModelProperty("圈子名称")
    private String name;

    @ApiModelProperty("圈子描述")
    private String description;

    @ApiModelProperty("圈子头像url")
    private String avatarUrl;

    @ApiModelProperty("圈子背景图片url")
    private String bgImgUrl;
}
