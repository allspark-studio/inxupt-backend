package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

@Data
public class ReplyNoticeVO {

    private Integer senderId;

    private String sourceContent;

    private String replyContent;

    private String sendTime;

    private Integer sourceType;

    private Integer postType;

    private Integer sourceId;

}
