package com.allsparkstudio.zaixiyou.pojo.RedisEntity;

import lombok.Data;

/**
 * 当日用户成长数据
 * @author AlkaidChen
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

    public UserDailyStatistics() {
        this.exp = 0;
        this.login = 0;
        this.postNum = 0;
        this.commentNum = 0;
    }
}
