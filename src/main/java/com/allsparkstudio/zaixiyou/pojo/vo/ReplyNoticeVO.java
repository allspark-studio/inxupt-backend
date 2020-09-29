package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

@Data
public class ReplyNoticeVO {

    private String senderName;

    private String senderAvatar;

    private String sourceContent;

    private String replyContent;

    private String sendTime;

    private Integer sourceType;

    private Integer sourceId;

}
