package com.allsparkstudio.zaixiyou.consumer;

import com.allsparkstudio.zaixiyou.consts.ExperienceConst;
import com.allsparkstudio.zaixiyou.dao.DailyStatisticsMapper;
import com.allsparkstudio.zaixiyou.dao.UserMapper;
import com.allsparkstudio.zaixiyou.enums.AddExpEnum;
import com.allsparkstudio.zaixiyou.enums.UserContentStateEnum;
import com.allsparkstudio.zaixiyou.pojo.po.DailyStatistics;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import com.allsparkstudio.zaixiyou.util.UserDailyStatisticsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

@Slf4j
@Component
public class DailyStatisticsConsumers {

    @Autowired
    private UserDailyStatisticsUtils userDailyStatisticsUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DailyStatisticsMapper dailyStatisticsMapper;

    /**
     * MQ延迟处理jwt验证成功后更新日活数据和更新用户最后登录时间
     *
     * @param user 从消息队列中传过来的user对象
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"userLogin"}
            )})
    public void login(User user) {
        log.debug("消费者routerKey=userActive收到消息");
        // 更新用户最后登录时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        user.setLastActiveTime(simpleDateFormat.format(new Date()));
        userMapper.updateLastActiveTime(user);
        // 加锁下面操作，防止多插入
        synchronized (this) {
            if (!userDailyStatisticsUtils.isLogin(user.getId())) {
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                String today = simpleDateFormat.format(new Date());
                DailyStatistics dailyStatistics = dailyStatisticsMapper.selectByDate(today);
                userDailyStatisticsUtils.updateLogin(user.getId());
                addExp(user.getId(), AddExpEnum.LOGIN.getExp());
                addCoin(user.getId(), new Random().nextInt(4) + 2);
                if (dailyStatistics == null) {
                    dailyStatistics = new DailyStatistics();
                    dailyStatistics.setDate(today);
                    dailyStatistics.setActiveUserNum(1);
                    int result = dailyStatisticsMapper.insertSelective(dailyStatistics);
                    if (result != 1) {
                        log.error("更新日活数据失败，数据库表'daily_statistics'插入失败");
                    }
                } else {
                    dailyStatistics.setActiveUserNum(dailyStatistics.getActiveUserNum() + 1);
                    int result = dailyStatisticsMapper.updateActiveUserNum(dailyStatistics);
                    if (result != 1) {
                        log.error("更新日活数据失败，数据库表'daily_statistics'更新失败");
                    }
                }
            }
        }
    }

    /**
     * MQ延迟统计当日注册用户量
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    //
                    key = {"userRegister"}
            )})
    public void register(User user) {
        // 记录当天注册用户数量
        log.debug("消费者routerKey=userRegister收到消息");
        synchronized (this) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            DailyStatistics dailyStatistics = dailyStatisticsMapper.selectByDate(simpleDateFormat.format(new Date()));
            if (dailyStatistics == null) {
                dailyStatistics = new DailyStatistics();
                dailyStatistics.setDate(simpleDateFormat.format(new Date()));
                dailyStatistics.setRegisterUserNum(1);
                dailyStatisticsMapper.insertSelective(dailyStatistics);
            } else {
                dailyStatistics.setRegisterUserNum(dailyStatistics.getRegisterUserNum() + 1);
                dailyStatisticsMapper.updateRegisterUserNum(dailyStatistics);
            }
        }
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    //
                    key = {"getLike"}
            )})
    public void getLiked(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        user.setLikeNum(user.getLikeNum() + 1);
        userMapper.updateLikeNum(user);
        addExp(userId, AddExpEnum.GET_LIKE.getExp());

    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    //
                    key = {"getFavorite"}
            )})
    public void getFavorite(Integer userId) {
        addExp(userId, AddExpEnum.GET_FAVORITE.getExp());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    //
                    key = {"getFollow"}
            )})
    public void getFollow(Integer userId) {
        addExp(userId, AddExpEnum.GET_FOLLOWED.getExp());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    //
                    key = {"getCoin"}
            )})
    public void getCoin(Integer userId) {
        userDailyStatisticsUtils.updatePostNum(userId);
        addExp(userId, AddExpEnum.GET_COIN.getExp());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    //
                    key = {"addPost"}
            )})
    public void addPost(Integer userId) {
        userDailyStatisticsUtils.updatePostNum(userId);
        addExp(userId, AddExpEnum.POST.getExp());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    //
                    key = {"addComment"}
            )})
    public void addComment(Integer userId) {
        userDailyStatisticsUtils.updateCommentNum(userId);
        addExp(userId, AddExpEnum.COMMENT.getExp());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    //
                    key = {"getComment"}
            )})
    public void getComment(Integer userId) {
        addExp(userId, AddExpEnum.GET_COMMENT.getExp());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    //
                    key = {"coin"}
            )})
    public void coin(Integer userId) {
        addExp(userId, AddExpEnum.COIN.getExp());
    }

    private void addExp(Integer userId, Integer exp) {
        synchronized (this) {
            User user = userMapper.selectByPrimaryKey(userId);
            if (!userDailyStatisticsUtils.isExpLimited(user.getId())) {
                userDailyStatisticsUtils.updateExp(userId, exp);
                user.setExperience(user.getExperience() + exp);
                if (user.getExperience() >= ExperienceConst.getExpByLv(user.getLevel())) {
                    user.setLevel(user.getLevel() + 1);
                }
                userMapper.updateExpAndLv(user);
            }
        }
    }

    private void addCoin(Integer userId, Integer coinNum) {
        synchronized (this) {
            User user = userMapper.selectByPrimaryKey(userId);
            user.setInsertableCoins(user.getInsertableCoins() + coinNum);
            userMapper.updateExpAndLv(user);
        }
    }
}
