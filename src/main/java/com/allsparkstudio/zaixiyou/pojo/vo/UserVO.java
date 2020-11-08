package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserVO {

    private Integer id;

    private String nickName;

    private String avatarUrl;

    private List<String> accountAuth;

    private String description;

    private Boolean followed;

    private Boolean selected;
}
