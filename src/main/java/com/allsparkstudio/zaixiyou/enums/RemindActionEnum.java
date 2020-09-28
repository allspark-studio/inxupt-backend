package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

@Getter
public enum RemindActionEnum {

    LIKE_POST(1),

    LIKE_COMMENT(2),

    COIN_POST(3),

    COIN_COMMENT(4),

    FAVORITE_POST(5),

    REPLY_POST(6),

    REPLY_COMMENT(7),

    FOLLOW(8),

    AT(9);

    Integer code;

    RemindActionEnum(Integer code) {
        this.code = code;
    }
}
