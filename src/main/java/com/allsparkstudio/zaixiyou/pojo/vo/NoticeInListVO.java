package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

/**
 * @author 陈帅
 * @date 2020/9/1
 */
@Data
public class NoticeInListVO {

    private Integer id;

    private String title;

    private String body;

    private String publishTime;

    private Integer authorId;

    private String authorName;

    private Boolean top;
}
