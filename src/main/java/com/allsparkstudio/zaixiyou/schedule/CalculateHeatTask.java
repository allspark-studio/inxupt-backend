package com.allsparkstudio.zaixiyou.schedule;

import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.pojo.po.Circle;
import com.allsparkstudio.zaixiyou.pojo.po.Comment;
import com.allsparkstudio.zaixiyou.pojo.po.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CalculateHeatTask {

    @Autowired
    PostMapper postMapper;

    @Autowired
    UserPostMapper userPostMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    UserCommentMapper userCommentMapper;

    @Autowired
    CircleMapper circleMapper;

    @Autowired
    UserCircleMapper userCircleMapper;

    @Autowired
    PostCircleMapper postCircleMapper;

    private static final Double G = 1.5;

    /**
     * heat = (likesNum*10 + coinsNum*20 + favoritesNum *20 + commentsNum *30) / (hour + 1)^G
     * like: 点赞数
     * coins: 投币数
     * comments: 评论数
     * favoritesNum: 收藏数
     * hour: 发布到现在的时间间隔,单位小时,+1防止除数太小
     * G: 重力加速度，它的数值大小决定了排名随时间下降的速度快慢，通常取1.5/1.8/2
     * <p>
     * 每小时执行一次
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void calculatePostHeat() {
        log.info("执行定时任务计算帖子热度");
        List<Post> postList = postMapper.selectAllByTime();
        for (Post post : postList) {
            int likesNum = userPostMapper.countLikeByPostId(post.getId());
            int coinsNum = userPostMapper.countCoinsByPostId(post.getId());
            int favoritesNum = userPostMapper.countFavoriteByPostId(post.getId());
            int commentsNum = commentMapper.countCommentsByPostId(post.getId());
            int hour = (int) ((System.currentTimeMillis() - post.getCreateTime().getTime()) / (1000 * 60 * 60));
            Integer heat = (likesNum * 10 + coinsNum * 20 + favoritesNum * 20 + commentsNum * 30) / (int) Math.pow(hour + 1, G);
            post.setHeat(heat);
            postMapper.updateHeat(post);
        }
    }

    /**
     * heat = (likesNum*10 + coinsNum*20 + subCommentsNum *30) / (hour + 1)^G
     * like: 点赞数
     * coins: 投币数
     * comments: 评论数
     * hour: 发布到现在的时间间隔,单位小时,+1防止除数太小
     * G: 重力加速度，它的数值大小决定了排名随时间下降的速度快慢，通常取1.5/1.8/2
     * <p>
     * 每小时执行一次
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void calculateCommentHeat() {
        log.info("执行定时任务计算评论热度");
        List<Comment> commentList = commentMapper.selectAll();
        for (Comment comment : commentList) {
            int likesNum = userCommentMapper.countLikeByCommentId(comment.getId());
            int coinsNum = userCommentMapper.countCoinsByCommentId(comment.getId());
            int commentsNum = commentMapper.countSubCommentsByCommentId(comment.getId());
            int hour = (int) ((System.currentTimeMillis() - comment.getCreateTime().getTime()) / (1000 * 60 * 60));
            Integer heat = (likesNum * 10 + coinsNum * 20 + commentsNum * 30) / (int) Math.pow(hour + 1, G);
            comment.setHeat(heat);
            commentMapper.updateHeat(comment);
        }
    }

    /**
     * heat = membersNum * 3 + topicsNum * 4
     * membersNum: 成员数量
     * topicsNum: 话题数量
     * 每小时执行一次
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void calculateCircleHeat() {
        log.info("执行定时任务计算圈子热度");
        List<Circle> circleList = circleMapper.selectAll();
        for (Circle circle : circleList) {
            int membersNum = userCircleMapper.countMembers(circle.getId());
            int topicsNum = postCircleMapper.countPostsByCircleId(circle.getId());
            Integer heat = membersNum * 30 + topicsNum * 40;
            circle.setHeat(heat);
            circleMapper.updateHeat(circle);
        }
    }
}
