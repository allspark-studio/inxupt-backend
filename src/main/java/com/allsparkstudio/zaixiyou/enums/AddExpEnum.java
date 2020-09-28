package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

@Getter
public enum AddExpEnum {
    LOGIN(2),
    GET_LIKE(1),
    GET_FAVORITE(2),
    GET_COMMENT(2),
    GET_FOLLOWED(3),
    GET_COIN(2),
    ADD_POST(3),
    ADD_COMMENT(2),
    ADD_CIRCLE(5),
    ADD_ANNOUNCEMENT(3),
    INSERT_COIN(3);

    Integer exp;

    AddExpEnum(Integer exp) {
        this.exp = exp;
    }
}
