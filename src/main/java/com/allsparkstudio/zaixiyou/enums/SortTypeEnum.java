package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

@Getter
public enum SortTypeEnum {

    TIME(1),
    HEAT(2);

    Integer code;

    SortTypeEnum(Integer code) {
        this.code = code;
    }
}
