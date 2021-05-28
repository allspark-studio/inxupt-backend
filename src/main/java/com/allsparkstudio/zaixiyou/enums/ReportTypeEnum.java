package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

@Getter
public enum  ReportTypeEnum {

    POST(1),
    COMMENT(2),
    USER(3);

    Integer code;

    ReportTypeEnum(Integer code) {
        this.code = code;
    }
}
