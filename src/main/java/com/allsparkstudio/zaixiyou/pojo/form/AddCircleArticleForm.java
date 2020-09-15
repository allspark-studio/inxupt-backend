package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 陈帅
 * @date 2020/8/26
 */

@Data
@ApiModel("圈子内发布文章")
public class AddCircleArticleForm {

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("正文")
    private String body;

    @ApiModelProperty("文章封面")
    private String cover;

    @ApiModelProperty("文章纯文字")
    private String pureText;

}
