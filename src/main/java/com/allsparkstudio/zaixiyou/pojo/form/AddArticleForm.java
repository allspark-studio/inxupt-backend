package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 陈帅
 * @date 2020/8/26
 */
@Data
@ApiModel("发布文章")
public class AddArticleForm {

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("正文")
    private String body;

    @ApiModelProperty("纯文本")
    private String pureText;

    @ApiModelProperty("封面")
    private String cover;

    @ApiModelProperty("主标签【id】列表")
    private List<Integer> mainTagIds;

    @ApiModelProperty("自定义标签【名字】列表")
    private List<String> customTags;

    @ApiModelProperty("@的用户id列表")
    private List<Integer> atIds;

    @ApiModelProperty("同步到的圈子id列表")
    private List<Integer> circleIds;

}
