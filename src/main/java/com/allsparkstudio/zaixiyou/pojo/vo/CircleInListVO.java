package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

/**
 * @author AlkaidChen
 * @date 2020/8/24
 */

@Data
public class CircleInListVO {

    private Integer id;

    private String name;

    private String avatarUrl;

    private String description;

    private Integer members;

    private Integer topics;

    private Boolean selected;
}
