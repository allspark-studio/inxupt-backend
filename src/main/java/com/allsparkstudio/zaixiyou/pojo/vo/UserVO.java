package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserVO {

    private Integer id;

    // 昵称
    private String nickName;

    // 头像链接
    private String avatarUrl;

    // 用户身份认证（平台授予）
    private List<String> accountAuth;

    // 用户描述，个性签名
    private String description;

    @Deprecated
    // 是否已关注
    private Boolean followed;

    // 用户等级
    private Integer level;
}
