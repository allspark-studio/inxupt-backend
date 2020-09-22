package com.allsparkstudio.zaixiyou.pojo.RedisEntity;

import lombok.Data;

/**
 * 当日用户成长数据
 * @author 陈帅
 * @date 2020/9/18
 */
@Data
public class UserDailyStatistics {

    /**
     * 当日获取经验值
     */
    private Integer exp;

    /**
     * 当日用户是否已登录
     */
    private Integer login;

    /**
     * 发帖子次数
     */
    private Integer postNum;

    /**
     * 发表评论次数
     */
    private Integer commentNum;

    /**
     * 创建圈子次数
     */
    private Integer circleNum;

    /**
     * 发布公告次数
     */
    private Integer circleNoticeNum;

    public UserDailyStatistics() {
        this.exp = 0;
        this.login = 0;
        this.postNum = 0;
        this.commentNum = 0;
        this.circleNum = 0;
        this.circleNoticeNum = 0;
    }
}
