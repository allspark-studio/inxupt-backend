package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

@Getter
public enum UserDailyLimitEnum {
    EXP(300),

    LOGIN(1),

    POST_NUM(30),

    COMMENT_NUM(100),

    CIRCLE_NUM(20),

    CIRCLE_NOTICE_NUM(30);

    Integer limit;

    UserDailyLimitEnum(Integer limit) {
        this.limit = limit;
    }
}
