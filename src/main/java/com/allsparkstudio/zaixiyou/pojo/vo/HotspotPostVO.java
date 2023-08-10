package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wzr
 * @date 2023/7/18
 */
@Data
public class HotspotPostVO {
    /**
     * 用户ID
     */
    private Integer id;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像链接
     */
    private String avatarUrl;
    /**
     * 用户身份认证（平台授予）
     */
    private List<String> accountAuth;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 发布时间
     */
    private String createTime;
    /**
     * 点赞数
     */
    private Integer likeNum;
    /**
     * 收藏数
     */
    private Integer favoriteNum;
    /**
     * 评论数
     */
    private Integer commentNum;
    /**
     * 是否点赞
     */
    private boolean liked;
    /**
     * 是否收藏
     */
    private boolean favorited;
    /**
     * 是否转发
     */
    private boolean forwarded;
    /**
     * 文章内容
     */
    private String postBody;
    /**
     * 文章照片或视频url
     */
    private String postMediaUrls;
}
