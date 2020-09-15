package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 陈帅
 * @date 2020/8/19
 */
@Data
public class SubCommentVO {
    private Integer commentId;

    private Integer rootId;

    private Integer parentId;

    private String text;

    private List<String> mediaUrls;

    private Integer state;

    private Boolean privately;

    private Integer authorId;

    private String authorNickname;

    private String authorAvatar;

    private Integer authorLevel;

    private Integer replyUserId;

    private String replyUserNickname;

    private String createTime;

    private Integer likeNum;

    private Boolean liked;

    private Integer coinsNum;

    private Boolean coined;
}
