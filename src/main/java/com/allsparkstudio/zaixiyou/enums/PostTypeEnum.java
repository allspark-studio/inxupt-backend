package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

/**帖子类型枚举  1帖子  2学术文章
 * @author 陈帅
 * @date 2020/8/20
 */
@Getter
public enum PostTypeEnum {

    POST(1, "帖子"),

    ARTICLE(2, "文章");

    Integer code;

    String type;

    PostTypeEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }
}
