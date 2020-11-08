package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

@Getter
public enum ClubTypeEnum {

    /**
     * 社团
     */
    CLUB(1),

    /**
     * 实验室
     */
    LAB(2);

    Integer code;

    ClubTypeEnum(Integer code) {
        this.code = code;
    }
}
