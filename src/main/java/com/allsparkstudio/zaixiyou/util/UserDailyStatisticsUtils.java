package com.allsparkstudio.zaixiyou.util;

import com.allsparkstudio.zaixiyou.enums.UserDailyLimitEnum;
import com.allsparkstudio.zaixiyou.pojo.RedisEntity.UserDailyStatistics;
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

    private static final String KEY = "userDailyStatistics";

    public boolean isLogin(Integer userId) {
        UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
        if (userDailyStatistics == null) {
            return false;
        }
        return UserDailyLimitEnum.LOGIN.getLimit().equals(userDailyStatistics.getLogin());
    }

    public boolean isAddExpLimited(Integer userId) {
        UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
        if (userDailyStatistics == null) {
            return false;
        }
        return userDailyStatistics.getExp() >= UserDailyLimitEnum.EXP.getLimit();
    }

    public boolean isAddPostLimited(Integer userId) {
        UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
        if (userDailyStatistics == null) {
            return false;
        }
        return userDailyStatistics.getPostNum() >= UserDailyLimitEnum.POST_NUM.getLimit();
    }

    public boolean isAddCommentLimited(Integer userId) {
        UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
        if (userDailyStatistics == null) {
            return false;
        }
        return userDailyStatistics.getCommentNum() >= UserDailyLimitEnum.COMMENT_NUM.getLimit();
    }

    public boolean isAddCircleLimited(int userId) {
        UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
        if (userDailyStatistics == null) {
            return false;
        }
        return userDailyStatistics.getCircleNum() >= UserDailyLimitEnum.CIRCLE_NUM.getLimit();
    }

    public boolean isAddAnnouncementLimited(int userId) {
        UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
        if (userDailyStatistics == null) {
            return false;
        }
        return userDailyStatistics.getCircleNum() >= UserDailyLimitEnum.ANNOUNCEMENT_NUM.getLimit();
    }

    public void updateLogin(Integer userId) {
        synchronized (this) {
            UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
            if (userDailyStatistics == null) {
                userDailyStatistics = new UserDailyStatistics();
                userDailyStatistics.setLogin(1);
            } else {
                userDailyStatistics.setLogin(1);
            }
            redisTemplate.opsForHash().put(KEY, userId, userDailyStatistics);
            redisTemplate.expireAt(KEY, getMidnight());
        }
    }

    public void updatePostNum(Integer userId) {
        synchronized (this) {
            UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
            if (userDailyStatistics == null) {
                userDailyStatistics = new UserDailyStatistics();
                userDailyStatistics.setPostNum(1);
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
                userDailyStatistics.setCommentNum(1);
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
                userDailyStatistics.setExp(exp);
            } else {
                userDailyStatistics.setExp(userDailyStatistics.getExp() + exp);
            }
            redisTemplate.opsForHash().put(KEY, userId, userDailyStatistics);
            redisTemplate.expireAt(KEY, getMidnight());
        }
    }

    public void updateCircleNum(Integer userId) {
        synchronized (this) {
            UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
            if (userDailyStatistics == null) {
                userDailyStatistics = new UserDailyStatistics();
                userDailyStatistics.setCircleNum(1);
            } else {
                userDailyStatistics.setCircleNum(userDailyStatistics.getCircleNum() + 1);
            }
            redisTemplate.opsForHash().put(KEY, userId, userDailyStatistics);
            redisTemplate.expireAt(KEY, getMidnight());
        }
    }

    public void updateAnnouncementNum(Integer userId) {
        synchronized (this) {
            UserDailyStatistics userDailyStatistics = (UserDailyStatistics) redisTemplate.opsForHash().get(KEY, userId);
            if (userDailyStatistics == null) {
                userDailyStatistics = new UserDailyStatistics();
                userDailyStatistics.setAnnouncementNum(1);
            } else {
                userDailyStatistics.setAnnouncementNum(userDailyStatistics.getAnnouncementNum() + 1);
            }
            redisTemplate.opsForHash().put(KEY, userId, userDailyStatistics);
            redisTemplate.expireAt(KEY, getMidnight());
        }
    }


    /**
     * 工具方法，生成当天晚上23:59:59的时间对象
     */
    private Date getMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
}
