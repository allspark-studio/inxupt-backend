package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

/**
 * @author AlkaidChen
 * @date 2020/8/26
 */

@Data
public class CircleMemberVO {

    private Integer id;

    private Integer role;

    private String nickName;

    private String description;

    private Boolean followed;

    private String avatarUrl;

}
