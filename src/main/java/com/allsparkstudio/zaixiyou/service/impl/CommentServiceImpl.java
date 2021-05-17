package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.enums.*;
import com.allsparkstudio.zaixiyou.pojo.form.AddCommentForm;
import com.allsparkstudio.zaixiyou.pojo.po.*;
import com.allsparkstudio.zaixiyou.pojo.vo.CommentVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.pojo.vo.SubCommentVO;
import com.allsparkstudio.zaixiyou.service.CommentService;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import com.allsparkstudio.zaixiyou.util.UserDailyStatisticsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author AlkaidChen
 * @date 2020/8/19
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserCommentLikeMapper userCommentLikeMapper;

    @Autowired
    private UserCommentCoinMapper userCommentCoinMapper;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserDailyStatisticsUtils userDailyStatisticsUtils;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseVO<List<CommentVO>> listAll(Integer postId, String token, Integer sortedBy) {
        Integer userId = null;
        boolean login = false;
        if (!StringUtils.isEmpty(token) && jwtUtils.validateToken(token)) {
            userId = jwtUtils.getIdFromToken(token);
            login = true;
        }
        // TODO:分页查询
        List<Comment> commentsList = new ArrayList<>();
        if (SortTypeEnum.TIME.getCode().equals(sortedBy)) {
            commentsList = commentMapper.selectByPostIdSortedByTime(postId);
        } else {
            commentsList = commentMapper.selectByPostIdSortedByHeat(postId);
        }
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : commentsList) {
            if (comment.getRootId() != 0) {
                continue;
            }
            CommentVO commentVO = new CommentVO();
            commentVO.setCommentId(comment.getId());
            commentVO.setRootId(comment.getRootId());
            commentVO.setParentId(comment.getParentId());
            commentVO.setText(comment.getBody());
            // TODO: 下个版本 => 评论添加视频或者图片
            // commentVO.setMediaUrls(null);
            commentVO.setAuthorId(comment.getAuthorId());
            User author = userMapper.selectByPrimaryKey(comment.getAuthorId());
            commentVO.setAuthorAvatar(author.getAvatarUrl());
            commentVO.setAuthorNickname(author.getNickname());
            commentVO.setAuthorLevel(author.getLevel());
            commentVO.setAccountAuth(Arrays.asList(author.getAccountAuth().split(";")));
            // TODO: 一次查询全部聚合结果
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            commentVO.setCreateTime(simpleDateFormat.format(comment.getCreateTime()));
            Integer likeNum = userCommentLikeMapper.countByCommentId(comment.getId());
            Integer coinsNum = userCommentCoinMapper.countByCommentId(comment.getId());
            commentVO.setLikeNum(likeNum);
            commentVO.setCoinsNum(coinsNum);
            boolean liked = false;
            boolean coined = false;
            if (login) {
                UserCommentLike userCommentLike = userCommentLikeMapper.selectByUserIdAndCommentId(userId, comment.getId());
                UserCommentCoin userCommentCoin = userCommentCoinMapper.selectByUserIdAndCommentId(userId, comment.getId());
                if (userCommentLike != null && userCommentLike.getState().equals(1)) {
                    liked = true;
                }
                if (userCommentCoin != null && userCommentCoin.getState().equals(1)) {
                    coined = true;
                }
            }
            commentVO.setLiked(liked);
            commentVO.setCoined(coined);
            commentVO.setState(comment.getState());
            commentVO.setPrivately(comment.getPrivately());
            List<SubCommentVO> subCommentList = new ArrayList<>();
            // 二级评论
            for (Comment subComment : commentsList) {
                if (!subComment.getRootId().equals(comment.getId())) {
                    continue;
                }
                SubCommentVO subCommentVO = new SubCommentVO();
                subCommentVO.setCommentId(subComment.getId());
                subCommentVO.setRootId(subComment.getRootId());
                subCommentVO.setParentId(subComment.getParentId());
                subCommentVO.setText(subComment.getBody());
                // TODO: 下个版本 => 评论添加视频或者图片
                // subCommentVO.setMediaUrls(null);
                subCommentVO.setAuthorId(subComment.getAuthorId());
                User subAuthor = userMapper.selectByPrimaryKey(subComment.getAuthorId());
                subCommentVO.setAuthorAvatar(subAuthor.getAvatarUrl());
                subCommentVO.setAuthorNickname(subAuthor.getNickname());
                subCommentVO.setAccountAuth(Arrays.asList(subAuthor.getAccountAuth().split(";")));
                subCommentVO.setReplyUserId(subComment.getReplyUserId());
                User replyUser = userMapper.selectByPrimaryKey(subComment.getReplyUserId());
                String replyUserNickname = "用户不存在";
                if (replyUser != null) {
                    replyUserNickname = replyUser.getNickname();
                }
                subCommentVO.setReplyUserNickname(replyUserNickname);
                subCommentVO.setAuthorLevel(subAuthor.getLevel());
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                subCommentVO.setCreateTime(simpleDateFormat.format(subComment.getCreateTime()));
                Integer subLikeNum = userCommentLikeMapper.countByCommentId(comment.getId());
                Integer subCoinsNum = userCommentCoinMapper.countByCommentId(comment.getId());
                subCommentVO.setLikeNum(subLikeNum);
                subCommentVO.setCoinsNum(subCoinsNum);
                boolean subLiked = false;
                boolean subCoined = false;
                if (login) {
                    UserCommentLike userCommentLike = userCommentLikeMapper.selectByUserIdAndCommentId(userId, comment.getId());
                    UserCommentCoin userCommentCoin = userCommentCoinMapper.selectByUserIdAndCommentId(userId, comment.getId());
                    if (userCommentLike != null && userCommentLike.getState().equals(1)) {
                        subLiked = true;
                    }
                    if (userCommentCoin != null && userCommentCoin.getState().equals(1)) {
                        subCoined = true;
                    }
                }
                subCommentVO.setLiked(subLiked);
                subCommentVO.setCoined(subCoined);
                subCommentVO.setState(subComment.getState());
                subCommentVO.setPrivately(subComment.getPrivately());
                subCommentList.add(subCommentVO);
            }
            commentVO.setSubComments(subCommentList);
            commentVOList.add(commentVO);
        }
        return ResponseVO.success(commentVOList);
    }

    @Override
    public ResponseVO<CommentVO> addComment(Integer postId, String token, AddCommentForm addCommentForm) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post == null) {
            log.error("请求的帖子不存在, postId:[{}]", postId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "帖子不存在");
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        User user = userMapper.selectByPrimaryKey(userId);
        if (UserStateEnum.MUTE.getCode().equals(user.getState())) {
            return ResponseVO.error(ResponseEnum.MUTE, "您已被禁言");
        }
        if (userDailyStatisticsUtils.isAddCommentLimited(userId)) {
            return ResponseVO.error(ResponseEnum.REACH_PUBLISH_LIMIT);
        }
        Comment comment = new Comment();
        comment.setRootId(addCommentForm.getRootId());
        comment.setParentId(addCommentForm.getParentId());
        comment.setAuthorId(userId);
        comment.setPostId(postId);
        comment.setReplyUserId(addCommentForm.getReplyUserId());
        comment.setBody(addCommentForm.getBody());
        comment.setPrivately(addCommentForm.getPrivately());
        int result1 = commentMapper.insertSelective(comment);
        if (result1 != 1) {
            log.error("发表评论时出现错误，数据库表'comment'插入失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        // MQ更新用户和经验值和当天发表评论数量
        rabbitTemplate.convertAndSend("dailyStatisticsExchange", "addComment", userId);
        rabbitTemplate.convertAndSend("dailyStatisticsExchange", "getComment", addCommentForm.getReplyUserId());

        // 构建事件提醒对象
        EventRemind remind = new EventRemind();
        remind.setSourceId(postId);
        remind.setSenderId(userId);
        remind.setPostType(post.getType());
        remind.setReplyContent(addCommentForm.getBody().length() > 20 ? addCommentForm.getBody().substring(0, 20) + "..." : addCommentForm.getBody());
        if (addCommentForm.getParentId().equals(0)) {
            if (PostTypeEnum.POST.getCode().equals(post.getType())) {
                StringBuilder sourceContentBuilder = new StringBuilder();
                if (post.getBody() != null) {
                    sourceContentBuilder.append(post.getBody().length() > 20 ? post.getBody().substring(0, 20) + "..." : post.getBody());
                }
                if (post.getPostMediaUrls() != null) {
                    for (int i = 0; i < post.getPostMediaUrls().split(";").length; i++) {
                        sourceContentBuilder.append("[图片]");
                    }
                }
                remind.setSourceContent(sourceContentBuilder.toString());
            } else {
                remind.setSourceContent(post.getArticleTitle());
            }
            // MQ通知用户帖子被回复
            remind.setAction(RemindActionEnum.REPLY_POST.getCode());
            remind.setReceiveId(post.getAuthorId());
            rabbitTemplate.convertAndSend("eventRemind", remind);
            // MQ更新帖子热度
            rabbitTemplate.convertAndSend("updateHeat", "post", postId);
        } else {
            // MQ更新评论热度
            rabbitTemplate.convertAndSend("updateHeat", "comment", addCommentForm.getRootId());
            // MQ通知用户帖子被回复
            remind.setAction(RemindActionEnum.REPLY_COMMENT.getCode());
            Comment parentComment = commentMapper.selectByPrimaryKey(addCommentForm.getParentId());
            if (parentComment.getBody() != null) {
                remind.setSourceContent(parentComment.getBody().length() > 20 ? parentComment.getBody().substring(0, 20) + "..." : parentComment.getBody());
            }
            remind.setReceiveId(parentComment.getAuthorId());
            rabbitTemplate.convertAndSend("eventRemind", remind);
        }
        CommentVO commentVO = new CommentVO();
        commentVO.setText(comment.getBody());
        // TODO: 下个版本加图片
//        commentVO.setMediaUrls(comment.getMediaUrls());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        commentVO.setCreateTime(simpleDateFormat.format(new Date()));
        commentVO.setLiked(false);
        commentVO.setCoined(false);
        commentVO.setLikeNum(0);
        commentVO.setCoinsNum(0);
        commentVO.setRootId(comment.getRootId());
        commentVO.setParentId(comment.getParentId());
        commentVO.setPrivately(comment.getPrivately());
        commentVO.setState(comment.getState());
        commentVO.setCommentId(comment.getId());
        commentVO.setAuthorId(comment.getAuthorId());
        commentVO.setAuthorAvatar(user.getAvatarUrl());
        commentVO.setAuthorNickname(user.getNickname());
        commentVO.setAuthorLevel(user.getLevel());
        commentVO.setAccountAuth(Arrays.asList(user.getAccountAuth().split(";")));
        commentVO.setCoined(false);
        commentVO.setSubComments(new ArrayList<>());
        return ResponseVO.success(commentVO);
    }

    @Override
    public ResponseVO deleteComment(Integer commentId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        if (comment == null) {
            log.error("请求的评论不存在, commentId:[{}]", commentId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "评论不存在");
        }
        if (!userId.equals(comment.getAuthorId())) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION, "没有权限");
        }
        int result1 = commentMapper.deleteByPrimaryKey(commentId);
        if (result1 != 1) {
            log.error("删除评论失败, userId:[{}], commentId:[{}]", userId, commentId);
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        // 删用户-评论-点赞表
        userCommentLikeMapper.deleteByCommentId(commentId);
        // 删用户-评论-投币表
        userCommentCoinMapper.deleteByCommentId(commentId);
        List<Comment> subCommentList = commentMapper.selectSubComments(commentId);
        for (Comment subComment : subCommentList) {
            // 删用户-评论-点赞表
            userCommentLikeMapper.deleteByCommentId(subComment.getId());
            // 删用户-评论-投币表
            userCommentCoinMapper.deleteByCommentId(subComment.getId());
            commentMapper.deleteByPrimaryKey(subComment.getId());
        }
        return ResponseVO.success();
    }

    @Override
    public ResponseVO like(Integer commentId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        if (comment == null) {
            log.error("请求的评论不存在, commentId:[{}]", commentId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "评论不存在");
        }
        UserCommentLike userCommentLike = userCommentLikeMapper.selectByUserIdAndCommentId(userId, commentId);
        if (userCommentLike == null) {
            userCommentLike = new UserCommentLike();
            userCommentLike.setUserId(userId);
            userCommentLike.setCommentId(commentId);
            userCommentLike.setState(1);
            int result = userCommentLikeMapper.insertSelective(userCommentLike);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
            // MQ增加经验值
            rabbitTemplate.convertAndSend("dailyStatisticsExchange", "getLike", comment.getAuthorId());
            // MQ通知作者内容被点赞
            EventRemind remind = new EventRemind();
            remind.setAction(RemindActionEnum.LIKE_COMMENT.getCode());
            Post post = postMapper.selectByPrimaryKey(comment.getPostId());
            remind.setPostType(post.getType());
            if (comment.getBody() != null) {
                remind.setSourceContent(comment.getBody().length() > 20 ? comment.getBody().substring(0, 20) + "..." : comment.getBody());
            }
            remind.setSourceId(comment.getPostId());
            remind.setSenderId(userId);
            remind.setReceiveId(comment.getAuthorId());
            rabbitTemplate.convertAndSend("eventRemind", remind);
        } else {
            userCommentLike.setState(1);
            int result = userCommentLikeMapper.updateByPrimaryKeySelective(userCommentLike);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        rabbitTemplate.convertAndSend("updateHeat", "comment", commentId);
        return ResponseVO.success();
    }

    @Override
    public ResponseVO dislike(Integer commentId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        if (comment == null) {
            log.error("请求的评论不存在, postId:[{}]", commentId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "评论不存在");
        }
        UserCommentLike userCommentLike = userCommentLikeMapper.selectByUserIdAndCommentId(userId, commentId);
        if (userCommentLike == null) {
            userCommentLike = new UserCommentLike();
            userCommentLike.setUserId(userId);
            userCommentLike.setCommentId(commentId);
            userCommentLike.setState(0);
            int result = userCommentLikeMapper.insertSelective(userCommentLike);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        } else {
            userCommentLike.setState(0);
            int result = userCommentLikeMapper.updateByPrimaryKeySelective(userCommentLike);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        return ResponseVO.success();
    }

    @Override
    public ResponseVO coin(Integer commentId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        if (comment == null) {
            log.error("请求的评论不存在, commentId:[{}]", commentId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "评论不存在");
        }
        User user = userMapper.selectByPrimaryKey(userId);
        if (userId.equals(comment.getAuthorId())) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION, "不能给自己投币哦");
        }
        if (user.getInsertableCoins() <= 0) {
            return ResponseVO.error(ResponseEnum.COINS_NOT_ENOUGH);
        }

        user.setInsertableCoins(user.getInsertableCoins() - 1);
        User author = userMapper.selectByPrimaryKey(comment.getAuthorId());
        author.setExchangeableCoins(author.getExchangeableCoins() + 1);
        userMapper.updateInsertableCoins(user);
        userMapper.updateExchangeableCoins(author);

        UserCommentCoin userCommentCoin = userCommentCoinMapper.selectByUserIdAndCommentId(userId, commentId);
        if (userCommentCoin == null) {
            userCommentCoin = new UserCommentCoin();
            userCommentCoin.setUserId(userId);
            userCommentCoin.setCommentId(commentId);
            userCommentCoin.setState(1);
            int result = userCommentCoinMapper.insertSelective(userCommentCoin);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
            // MQ增加经验值
            rabbitTemplate.convertAndSend("dailyStatisticsExchange", "getLike", comment.getAuthorId());
            // MQ通知作者内容被投币
            EventRemind remind = new EventRemind();
            remind.setAction(RemindActionEnum.COIN_COMMENT.getCode());
            remind.setSourceContent(comment.getBody().length() > 20 ? comment.getBody().substring(0, 20) + "..." : comment.getBody());
            remind.setSourceId(comment.getPostId());
            remind.setSenderId(userId);
            Post post = postMapper.selectByPrimaryKey(comment.getPostId());
            remind.setPostType(post.getType());
            remind.setReceiveId(comment.getAuthorId());
            rabbitTemplate.convertAndSend("eventRemind", remind);
        } else {
            userCommentCoin.setState(1);
            int result = userCommentCoinMapper.updateByPrimaryKeySelective(userCommentCoin);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        rabbitTemplate.convertAndSend("updateHeat", "comment", commentId);
        return ResponseVO.success();
    }
}
