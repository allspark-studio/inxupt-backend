package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

@Getter
public enum UserDailyLimitEnum {
    EXP(300),

    LOGIN(1),

    POST_NUM(10),

    COMMENT_NUM(50),

    CIRCLE_NUM(5),

    ANNOUNCEMENT_NUM(10);

    Integer limit;

    UserDailyLimitEnum(Integer limit) {
        this.limit = limit;
    }
}
