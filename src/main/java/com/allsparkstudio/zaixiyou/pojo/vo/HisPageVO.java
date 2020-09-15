package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

/**
 * @author 陈帅
 * @date 2020/7/16
 */
@Data
public class HisPageVO {

    // 他的id
    private Integer hisId;
    // 昵称
    private String nickname;
    // 头像图片地址
    private String avatarUrl;
    // 个性描述
    private String description;
    // 性别
    private Integer gender;
    // 年级
    private String grade;
    // 专业
    private String major;
    // 等级
    private Integer level;
    // 获赞次数
    private Integer likedNum;
    // 关注他的粉丝数量
    private Integer fansNum;
    // 他关注的用户数量
    private Integer followNum;
    // 主页背景图片地址
    private String bgImageUrl;
    // 是否已经关注该用户
    private Boolean followed;
    // 动态数量
    private Integer newsNum;
    // 学术文章数量
    private Integer articleNum;
}
