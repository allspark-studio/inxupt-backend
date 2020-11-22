package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

@Getter
public enum AddExpEnum {
    // 每日登陆
    LOGIN(2),
    // 被点赞
    GET_LIKE(1),
    // 被收藏
    GET_FAVORITE(2),
    // 被评论
    GET_COMMENT(2),
    // 被关注
    GET_FOLLOWED(3),
    // 被投币
    GET_COIN(2),
    // 发帖子/文章
    ADD_POST(3),
    // 发评论
    ADD_COMMENT(2),
    // 新建圈子
    ADD_CIRCLE(5),
    // 发布公告
    ADD_ANNOUNCEMENT(3),
    // 投币
    INSERT_COIN(3);

    Integer exp;

    AddExpEnum(Integer exp) {
        this.exp = exp;
    }
}
