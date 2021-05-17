package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author AlkaidChen
 * @date 2020/8/19
 */
@Data
public class CommentVO {
    private Integer commentId;

    private Integer rootId;

    private Integer parentId;

    private String text;

    private List<String> mediaUrls;

    /**
     * state: 0正常 1作者删除 2违规删除
     */
    private Integer state;

    private Boolean privately;

    private Integer authorId;

    private String authorNickname;

    private String authorAvatar;

    private Integer authorLevel;

    private List<String> accountAuth;

    private String createTime;

    private Integer likeNum;

    private Integer coinsNum;

    private Boolean liked;

    private Boolean coined;

    private List<SubCommentVO> subComments;

}
