package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FoodVO {

    private Integer id;

    private String imgUrl;

    private String name;

    private BigDecimal price;

    private Boolean isLiked;

    private Integer likedNum;

}
