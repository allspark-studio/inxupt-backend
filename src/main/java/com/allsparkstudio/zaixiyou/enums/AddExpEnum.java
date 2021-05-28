package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

@Getter
public enum AddExpEnum {
    // 每日登陆
    LOGIN(5),
    // 被点赞
    GET_LIKE(1),
    // 被收藏
    GET_FAVORITE(1),
    // 被评论
    GET_COMMENT(1),
    // 被关注
    GET_FOLLOWED(2),
    // 被投币
    GET_COIN(2),
    // 发帖子/文章
    ADD_POST(2),
    // 发评论
    ADD_COMMENT(1),
    // 投币
    INSERT_COIN(2);

    Integer exp;

    AddExpEnum(Integer exp) {
        this.exp = exp;
    }
}
