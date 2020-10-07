package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

@Data
public class NewsNoticeVO {

    private Integer senderId;

    private Integer sourceId;

    private String action;

    private String sourceType;

    private Integer postType;

    private String sourceContent;

    private String sendTime;

}
