package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("提交反馈表单")
public class FeedbackForm {

    @ApiModelProperty("正文")
    private String body;

    @ApiModelProperty("联系方式")
    private String contact;
}
