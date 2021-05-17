package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

/**
 * @author AlkaidChen
 * @date 2020/9/1
 */
@Data
public class AnnouncementInListVO {

    private Integer id;

    private String title;

    private String body;

    private String publishTime;

    private Integer authorId;

    private String authorName;

    private Boolean top;
}
