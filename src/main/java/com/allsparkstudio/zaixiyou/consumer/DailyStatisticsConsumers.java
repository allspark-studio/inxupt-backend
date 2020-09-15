package com.allsparkstudio.zaixiyou.consumer;

import com.allsparkstudio.zaixiyou.dao.DailyStatisticsMapper;
import com.allsparkstudio.zaixiyou.dao.UserMapper;
import com.allsparkstudio.zaixiyou.pojo.po.DailyStatistics;
import com.allsparkstudio.zaixiyou.pojo.po.User;
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
import java.util.TimeZone;

@Slf4j
@Component
public class DailyStatisticsConsumers {

    @Autowired
    StringRedisTemplate redisTemplate;

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
                    key = {"userActive"}
            )})
    public void active(User user) {
        log.debug("消费者routerKey=userActive收到消息");
        // 更新用户最后登录时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        user.setLastActiveTime(simpleDateFormat.format(new Date()));
        userMapper.updateByPrimaryKeySelective(user);
        // 加锁下面操作，防止多插入
        synchronized (this) {
            if (!redisTemplate.opsForSet().isMember("activeUserIdSet", String.valueOf(user.getId()))) {
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                String today = simpleDateFormat.format(new Date());
                DailyStatistics dailyStatistics = dailyStatisticsMapper.selectByDate(today);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                Date midnight = calendar.getTime();
                log.debug(midnight.toString());
                redisTemplate.opsForSet().add("activeUserIdSet", String.valueOf(user.getId()));
                redisTemplate.expireAt("activeUserIdSet", midnight);
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
                    int result = dailyStatisticsMapper.updateByPrimaryKeySelective(dailyStatistics);
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
        log.debug("消费者routerKey=userRegister收到消息");
        // 记录当天注册用户数量
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        synchronized (this) {
            DailyStatistics dailyStatistics = dailyStatisticsMapper.selectByDate(simpleDateFormat.format(new Date()));
            if (dailyStatistics == null) {
                dailyStatistics = new DailyStatistics();
                dailyStatistics.setRegisterUserNum(1);
                dailyStatisticsMapper.insertSelective(dailyStatistics);
            } else {
                dailyStatistics.setRegisterUserNum(dailyStatistics.getRegisterUserNum() + 1);
                dailyStatisticsMapper.updateByPrimaryKeySelective(dailyStatistics);
            }
        }
    }
}
