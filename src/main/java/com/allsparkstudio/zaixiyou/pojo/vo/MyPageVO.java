package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

/**
 * @author 陈帅
 * @date 2020/7/16
 */
@Data
public class MyPageVO {

    // 昵称
    private String nickname;
    // 头像图片地址
    private String avatarUrl;
    // 个性描述
    private String description;
    // 等级
    private Integer level;
    // 性别
    private Integer gender;
    // 年级
    private String grade;
    // 专业
    private String major;
    // 经验
    private Integer experience;
    // 当前等级需要的经验
    private Integer needExperience;
    // 名片背景图片地址
    private String cartBgImageUrl;
    // 签到硬币数量
    private Integer signCoinsNum;
    // 可兑换硬币数量
    private Integer exchangeCoinsNum;
    // 动态数量
    private Integer newsNum;
    // 粉丝数量
    private Integer fansNum;
    // 关注数量
    private Integer followNum;
    // 收藏数量
    private Integer favoriteNum;
}
