package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

/**
 * 圈子用户权限
 * @author AlkaidChen
 * @date 2020/8/20
 */
@Getter
public enum UserCircleRoleEnum {

    UNFOLLOW(1, "未关注"),

    FOLLOW(2, "已关注"),

    ADMIN(3, "管理员"),

    OWNER(4, "圈主");

    Integer code;

    String role;

    UserCircleRoleEnum(int code, String role) {
        this.code = code;
        this.role = role;
    }
}
