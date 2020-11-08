package com.allsparkstudio.zaixiyou.consumer;

import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.pojo.po.Circle;
import com.allsparkstudio.zaixiyou.pojo.po.Comment;
import com.allsparkstudio.zaixiyou.pojo.po.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 计算热度的消费者
 * @author 陈帅
 */
@Component
@Slf4j
public class CalculateHeatConsumers {

    @Autowired
    PostMapper postMapper;

    @Autowired
    UserPostLikeMapper userPostLikeMapper;

    @Autowired
    UserPostFavoriteMapper userPostFavoriteMapper;

    @Autowired
    UserPostCoinMapper userPostCoinMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    UserCommentLikeMapper userCommentLikeMapper;

    @Autowired
    UserCommentCoinMapper userCommentCoinMapper;

    @Autowired
    CircleMapper circleMapper;

    @Autowired
    UserCircleMapper userCircleMapper;

    @Autowired
    PostCircleMapper postCircleMapper;

    private static final Double G = 1.2;

    /**
     * heat = (likesNum*10 + coinsNum*20 + favoritesNum *20 + commentsNum *30) / (hour + 1)^G
     * like: 点赞数
     * coins: 投币数
     * comments: 评论数
     * favoritesNum: 收藏数
     * hour: 发布到现在的时间间隔,单位小时,+1防止除数太小
     * G: 重力加速度，它的数值大小决定了排名随时间下降的速度快慢，通常取1.5/1.8/2
     * 每次更新影响热度相关的数据时更新一次
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "updateHeat"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"post"}
            )})
    public void calculatePostHeat(Integer postId) {
        int likesNum = userPostLikeMapper.countByPostId(postId);
        int coinsNum = userPostFavoriteMapper.countByPostId(postId);
        int favoritesNum = userPostCoinMapper.countByPostId(postId);
        int commentsNum = commentMapper.countCommentsByPostId(postId);
        Post post = postMapper.selectByPrimaryKey(postId);
        int hour = (int) ((System.currentTimeMillis() - post.getCreateTime().getTime()) / (1000 * 60 * 60));
        Integer heat = (likesNum * 10 + coinsNum * 20 + favoritesNum * 20 + commentsNum * 30) / (int) Math.pow(hour + 1, G);
        post.setHeat(heat);
        postMapper.updateHeat(post);
    }

    /**
     * heat = (likesNum*10 + coinsNum*20 + subCommentsNum *30) / (hour + 1)^G
     * like: 点赞数
     * coins: 投币数
     * comments: 评论数
     * hour: 发布到现在的时间间隔,单位小时,+1防止除数太小
     * G: 重力加速度，它的数值大小决定了排名随时间下降的速度快慢，通常取1.5/1.8/2
     * 每次更新影响热度相关的数据时更新一次
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "updateHeat"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"comment"}
            )})
    public void calculateCommentHeat(Integer commentId) {
        log.info("执行定时任务计算评论热度");
        int likesNum = userCommentLikeMapper.countByCommentId(commentId);
        int coinsNum = userCommentCoinMapper.countByCommentId(commentId);
        int commentsNum = commentMapper.countSubCommentsByCommentId(commentId);
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        int hour = (int) ((System.currentTimeMillis() - comment.getCreateTime().getTime()) / (1000 * 60 * 60));
        Integer heat = (likesNum * 10 + coinsNum * 20 + commentsNum * 30) / (int) Math.pow(hour + 1, G);
        comment.setHeat(heat);
        commentMapper.updateHeat(comment);
    }

    /**
     * heat = membersNum * 3 + topicsNum * 4
     * membersNum: 成员数量
     * topicsNum: 话题数量
     * 每次更新影响热度相关的数据时更新一次
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "updateHeat"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"circle"}
            )})
    public void calculateCircleHeat(Integer circleId) {
        log.info("执行定时任务计算圈子热度");
        int membersNum = userCircleMapper.countMembers(circleId);
        int topicsNum = postCircleMapper.countPostsByCircleId(circleId);
        Integer heat = membersNum * 30 + topicsNum * 40;
        Circle circle = circleMapper.selectByPrimaryKey(circleId);
        circle.setHeat(heat);
        circleMapper.updateHeat(circle);
    }
}
