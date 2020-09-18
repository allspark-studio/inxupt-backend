package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class MyPageInfo<T> {

    private Integer total;

    private List<T> list;

    private boolean isLastPage;
}
