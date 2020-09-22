package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 陈帅
 * @date 2020/8/20
 */
@Data
public class PostVO {

    private Integer postId;

    /**
     *   1:【帖子】
     *   2:【文章】
     */
    private Integer type;

    /**
     * 类型为【文章】才有的属性
     */
    private String title;

    /**
     * 类型为【文章】才有的属性
     */
    private String pureText;

    /**
     * 类型为【文章】才有的属性
     */
    private String cover;

    private String body;

    /**
     * 类型为【帖子】才有的属性
     */
    private List<String> mediaUrls;

    private List<Map<String, Object>> ats;

    private Integer authorId;

    private String authorName;

    private String authorAvatar;

    private Integer authorLevel;

    private String authorDescription;

    private String createTime;

    private List<Map<String, String>> tags;

    private Integer likeNum;

    private Integer commentNum;

    private Integer favoriteNum;

    private Integer coinsNum;

    private Boolean liked;

    private Boolean favorited;

    private Boolean coined;

}
