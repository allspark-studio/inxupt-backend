package com.allsparkstudio.zaixiyou.pojo.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class Category implements Serializable {
    private Integer id;

    private String name;

    private Integer state;
}