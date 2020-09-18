package com.allsparkstudio.zaixiyou.util;

import com.allsparkstudio.zaixiyou.enums.UserDailyLimitEnum;
import com.allsparkstudio.zaixiyou.pojo.RedisEntity.UserDailyStatistics;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Component
@Slf4j
public class UserDailyStatisticsUtils {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    private static final Gson gson = new Gson();

    private static final String KEY = "userDailyStatistics";

    public boolean isLogin(Integer userId) {
        UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
        if (userDailyStatistics == null) {
            return false;
        }
        return UserDailyLimitEnum.LOGIN.getLimit().equals(userDailyStatistics.getLogin());
    }

    public boolean isExpLimited(Integer userId) {
        UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
        if (userDailyStatistics == null) {
            return false;
        }
        return userDailyStatistics.getExp() >= UserDailyLimitEnum.EXP.getLimit();
    }

    public boolean isPostLimited(Integer userId) {
        UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
        if (userDailyStatistics == null) {
            return false;
        }
        return userDailyStatistics.getPostNum() >= UserDailyLimitEnum.POST_NUM.getLimit();
    }

    public boolean isCommentLimited(Integer userId) {
        UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
        if (userDailyStatistics == null) {
            return false;
        }
        return userDailyStatistics.getCommentNum() >= UserDailyLimitEnum.COMMENT_NUM.getLimit();
    }

    public void updateLogin(Integer userId) {
        synchronized (this) {
            UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
            if (userDailyStatistics == null) {
                userDailyStatistics = new UserDailyStatistics();
                userDailyStatistics.setExp(0);
                userDailyStatistics.setLogin(1);
                userDailyStatistics.setCommentNum(0);
                userDailyStatistics.setPostNum(0);
            } else {
                userDailyStatistics.setLogin(1);
            }
            redisTemplate.opsForHash().put(KEY, userId, userDailyStatistics);
            redisTemplate.expireAt(KEY, getMidnight());
        }
    }

    private Date getMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public void updatePostNum(Integer userId) {
        //
        synchronized (this) {
            UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
            if (userDailyStatistics == null) {
                userDailyStatistics = new UserDailyStatistics();
                userDailyStatistics.setPostNum(1);
                userDailyStatistics.setLogin(0);
                userDailyStatistics.setCommentNum(0);
                userDailyStatistics.setExp(0);
            } else {
                userDailyStatistics.setPostNum(userDailyStatistics.getPostNum() + 1);
            }
            redisTemplate.opsForHash().put(KEY, userId, userDailyStatistics);
            redisTemplate.expireAt(KEY, getMidnight());
        }
    }

    public void updateCommentNum(Integer userId) {
        synchronized (this) {
            UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
            if (userDailyStatistics == null) {
                userDailyStatistics = new UserDailyStatistics();
                userDailyStatistics.setPostNum(0);
                userDailyStatistics.setLogin(0);
                userDailyStatistics.setCommentNum(1);
                userDailyStatistics.setExp(0);
            } else {
                userDailyStatistics.setCommentNum(userDailyStatistics.getCommentNum() + 1);
            }
            redisTemplate.opsForHash().put(KEY, userId, userDailyStatistics);
            redisTemplate.expireAt(KEY, getMidnight());
        }
    }

    public void updateExp(Integer userId, Integer exp) {
        synchronized (this) {
            UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
            if (userDailyStatistics == null) {
                userDailyStatistics = new UserDailyStatistics();
                userDailyStatistics.setPostNum(0);
                userDailyStatistics.setLogin(0);
                userDailyStatistics.setCommentNum(1);
                userDailyStatistics.setExp(exp);
            } else {
                userDailyStatistics.setExp(userDailyStatistics.getExp() + exp);
            }
            redisTemplate.opsForHash().put(KEY, userId, userDailyStatistics);
            redisTemplate.expireAt(KEY, getMidnight());
        }
    }
}
