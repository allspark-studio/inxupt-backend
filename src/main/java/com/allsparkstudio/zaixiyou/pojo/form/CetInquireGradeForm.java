package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Marble
 * @Date: 2021/6/2 17:03
 * @Description: 查询四级成绩需要的信息
 */
@ApiModel("查询四级成绩需要的信息")
@Data
public class CetInquireGradeForm {
    @ApiModelProperty("准考证号")
    private String ticket;
    @ApiModelProperty("考生姓名")
    private String name;
}
