package com.allsparkstudio.zaixiyou.consumer;

import com.allsparkstudio.zaixiyou.consts.ExperienceConst;
import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.enums.AddExpEnum;
import com.allsparkstudio.zaixiyou.pojo.po.DailyStatistics;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import com.allsparkstudio.zaixiyou.util.UserDailyStatisticsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 每日统计的消费者
 *
 * @author 陈帅
 */
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
     * @param user 从消息队列中传过来的user对象
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    // 路由key
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
            // 如果用户当日为登录，即第一次登录，更新用户当日是否已登录
            // 如果要加入用户每日签到，可使用同样逻辑处理
            if (!userDailyStatisticsUtils.isLogin(user.getId())) {
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                String today = simpleDateFormat.format(new Date());
                DailyStatistics dailyStatistics = dailyStatisticsMapper.selectByDate(today);
                userDailyStatisticsUtils.updateLogin(user.getId());
                // 当日第一次登录增加经验值、硬币数
                addExp(user.getId(), AddExpEnum.LOGIN.getExp());
                addCoin(user.getId(), new Random().nextInt(4) + 2);
                // 如果当日还没有日统计数据记录，则新建一条记录并插入
                if (dailyStatistics == null) {
                    dailyStatistics = new DailyStatistics();
                    dailyStatistics.setDate(today);
                    dailyStatistics.setActiveUserNum(1);
                    int result = dailyStatisticsMapper.insertSelective(dailyStatistics);
                    if (result != 1) {
                        log.error("更新日活数据失败，数据库表'daily_statistics'插入失败");
                    }
                } else {
                    // 如果当日已经有日统计数据记录，则更新日活数量
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
                    // 路由key
                    key = {"userRegister"}
            )})
    public void register(User user) {
        log.debug("消费者routerKey=userRegister收到消息");
        // 锁住下面代码，防止多插入
        synchronized (this) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            DailyStatistics dailyStatistics = dailyStatisticsMapper.selectByDate(simpleDateFormat.format(new Date()));
            if (dailyStatistics == null) {
                // 如果没有当日统计数据记录，则插入一条
                dailyStatistics = new DailyStatistics();
                dailyStatistics.setDate(simpleDateFormat.format(new Date()));
                dailyStatistics.setRegisterUserNum(1);
                dailyStatisticsMapper.insertSelective(dailyStatistics);
            } else {
                // 如果有当日统计数据记录，则更新这条数据
                dailyStatistics.setRegisterUserNum(dailyStatistics.getRegisterUserNum() + 1);
                dailyStatisticsMapper.updateRegisterUserNum(dailyStatistics);
            }
        }
    }

    /**
     * 用户内容被点赞后的MQ延迟处理：
     * 增加经验、增加被点赞数
     * @param userId 被点赞用户id
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    // 路由key
                    key = {"getLike"}
            )})
    public void getLiked(Integer userId) {
        synchronized (this) {
            User user = userMapper.selectByPrimaryKey(userId);
            user.setLikeNum(user.getLikeNum() + 1);
            userMapper.updateLikeNum(user);
            addExp(userId, AddExpEnum.GET_LIKE.getExp());
        }
    }

    /**
     * 用户帖子被收藏后的MQ延迟处理：
     * 增加经验
     * @param userId 被收藏用户id
     */
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
                    key = {"getFollowed"}
            )})
    public void getFollow(Integer userId) {
        addExp(userId, AddExpEnum.GET_FOLLOWED.getExp());
    }

    /**
     * 用户帖子被投币后的MQ延迟处理：
     * 增加经验
     * @param userId 被投币用户id
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    // 路由key
                    key = {"getCoin"}
            )})
    public void getCoin(Integer userId) {
        userDailyStatisticsUtils.updatePostNum(userId);
        addExp(userId, AddExpEnum.GET_COIN.getExp());
    }

    /**
     * 用户发布帖子后的MQ延迟处理：
     * 增加经验、更新用户当日发帖子数量
     * @param userId 发帖用户id
     */
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
        addExp(userId, AddExpEnum.ADD_POST.getExp());
    }

    /**
     * 用户发布评论后的MQ延迟处理：
     * 增加经验、更新用户当日发评论数量
     * @param userId 评论用户id
     */
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
        addExp(userId, AddExpEnum.ADD_COMMENT.getExp());
    }

    /**
     * 用户内容被评论后的MQ延迟处理：
     * 增加经验
     * @param userId 被评论用户id
     */
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

    /**
     * 用户投币后的MQ延迟处理：
     * 增加经验
     * @param userId 投币用户id
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    //
                    key = {"insertCoin"}
            )})
    public void coin(Integer userId) {
        addExp(userId, AddExpEnum.INSERT_COIN.getExp());
    }

    /**
     * 用户新建圈子后的MQ延迟处理：
     * 增加经验，更新用户当日创建圈子数量
     * @param userId 创建圈子的用户id
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    //
                    key = {"addCircle"}
            )})
    public void addCircle(Integer userId) {
        userDailyStatisticsUtils.updateCircleNum(userId);
        addExp(userId, AddExpEnum.ADD_CIRCLE.getExp());
    }

    /**
     * 用户发布圈子公告后的MQ延迟处理：
     * 增加经验，更新用户当日发布圈子公告数量
     * @param userId 发布公告的用户id
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "dailyStatisticsExchange"),
                    //
                    key = {"addAnnouncement"}
            )})
    public void addAnnouncement(Integer userId) {
        userDailyStatisticsUtils.updateAnnouncementNum(userId);
        addExp(userId, AddExpEnum.ADD_ANNOUNCEMENT.getExp());
    }

    /**
     * 工具方法：加经验值
     * @param userId 用户id
     * @param exp 增加的经验值
     */
    private void addExp(Integer userId, Integer exp) {
        synchronized (this) {
            User user = userMapper.selectByPrimaryKey(userId);
            if (!userDailyStatisticsUtils.isAddExpLimited(user.getId())) {
                userDailyStatisticsUtils.updateExp(userId, exp);
                user.setExperience(user.getExperience() + exp);
                if (user.getExperience() >= ExperienceConst.getExpByLv(user.getLevel())) {
                    user.setLevel(user.getLevel() + 1);
                }
                userMapper.updateExpAndLv(user);
            }
        }
    }

    /**
     * 工具方法：加硬币
     * @param userId 用户id
     * @param coinNum 增加的硬币数量
     */
    private void addCoin(Integer userId, Integer coinNum) {
        synchronized (this) {
            User user = userMapper.selectByPrimaryKey(userId);
            user.setInsertableCoins(user.getInsertableCoins() + coinNum);
            userMapper.updateInsertableCoins(user);
        }
    }
}
