package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 陈帅
 * @date 2020/8/20
 */
@Data
@ApiModel("发布评论")
public class AddCommentForm {

    @ApiModelProperty("正文")
    private String body;

    /**
     * 如果parenId = 0，则为一级评论
     */

    @ApiModelProperty("子评论的顶级评论id，0表示评论为顶级评论")
    private Integer rootId;

    @ApiModelProperty("子评论的父评论id，0表示评论为父评论")
    private Integer parentId;

    @ApiModelProperty("子评论回复的用户的id")
    private Integer replyUserId;

    @ApiModelProperty("是否是私密评论")
    private Boolean privately;
}
