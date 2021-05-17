package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author AlkaidChen
 * @date 2020/8/26
 */

@Data
@ApiModel("圈子内发布帖子")
public class AddCirclePostForm {

    @ApiModelProperty("正文")
    private String body;

    @ApiModelProperty("图片或视频url列表")
    private List<String> mediaUrls;
}
