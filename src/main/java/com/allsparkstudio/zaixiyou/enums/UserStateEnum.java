package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

@Getter
public enum UserStateEnum {

    /**
     * 正常
     */
    NORMAL(0),

    MUTE(1),

    BANNED(2);

    Integer code;

    UserStateEnum(Integer code) {
        this.code = code;
    }
}
