package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 陈帅
 * @date 2020/8/21
 */

@Data
@ApiModel("发布帖子")
public class AddPostForm {

    @ApiModelProperty("正文")
    private String body;

    @ApiModelProperty("主标签【id】列表")
    private List<Integer> mainTagIds;

    @ApiModelProperty("自定义标签【名字】列表")
    private List<String> customTags;

    @ApiModelProperty("图片或视频url列表")
    private List<String> mediaUrls;

    @ApiModelProperty("@的用户id列表")
    private List<Integer> atIds;

    @ApiModelProperty("同步到的圈子id列表")
    private List<Integer> circleIds;
}
