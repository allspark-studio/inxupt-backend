package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

/**
 * @author 陈帅
 * @date 2020/8/28
 */
@Getter
public enum UserContentStateEnum {
    LIKE(1, "点赞"),

    FAVORITE(2, "收藏"),

    COIN(3, "投币");

    Integer code;

    String name;

    UserContentStateEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
