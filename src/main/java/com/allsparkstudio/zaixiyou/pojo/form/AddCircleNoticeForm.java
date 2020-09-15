package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 陈帅
 * @date 2020/8/25
 */

@Data
@ApiModel("发布圈子公告")
public class AddCircleNoticeForm {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("正文")
    private String body;

    @ApiModelProperty("图片或视频url列表")
    private List<String> mediaUrls;

    @ApiModelProperty("是否置顶")
    private Boolean top;

}
