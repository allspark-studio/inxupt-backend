package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wzr
 * @date 2023/7/18
 */
@Data
public class HotspotUserVO {
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
     * 粉丝数
     */
    private Integer fansNum;
    /**
     * 发布数
     */
    private Integer postNum;
    /**
     * 获赞数
     */
    private Integer likeNum;
    /**
     * 是否关注
     */
    private boolean followed;
}
