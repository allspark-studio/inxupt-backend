package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

@Data
public class FansVO {

    private Integer id;

    private String nickName;

    private String avatarUrl;

    private String description;

    private Boolean followed;

    private Boolean selected;
}
