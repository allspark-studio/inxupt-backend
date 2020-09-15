package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.enums.UserContentStateEnum;
import com.allsparkstudio.zaixiyou.pojo.form.AddCommentForm;
import com.allsparkstudio.zaixiyou.pojo.po.*;
import com.allsparkstudio.zaixiyou.pojo.vo.CommentVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.pojo.vo.SubCommentVO;
import com.allsparkstudio.zaixiyou.service.CommentService;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author 陈帅
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
    private UserCommentMapper userCommentMapper;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public ResponseVO<List<CommentVO>> listAll(Integer postId, String token) {
        Integer userId = null;
        boolean login = false;
        if (!StringUtils.isEmpty(token) && jwtUtils.validateToken(token)) {
            userId = jwtUtils.getIdFromToken(token);
            login = true;
        }
        // TODO:分页查询
        List<Comment> commentsList = commentMapper.selectByPostId(postId);
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
            // TODO: 一次查询全部聚合结果
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            commentVO.setCreateTime(simpleDateFormat.format(comment.getCreateTime()));
            Integer likeNum = userCommentMapper.countLikeByCommentId(comment.getId());
            Integer coinsNum = userCommentMapper.countCoinsByCommentId(comment.getId());
            commentVO.setLikeNum(likeNum);
            commentVO.setCoinsNum(coinsNum);
            Boolean liked = false;
            Boolean coined = false;
            if (login) {
                UserComment userComment = userCommentMapper.selectByUserIdAndCommentId(userId, comment.getId());
                if (userComment != null) {
                    liked = userComment.getLiked();
                    coined = userComment.getCoined();
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
                Integer subLikeNum = userCommentMapper.countLikeByCommentId(comment.getId());
                Integer subCoinsNum = userCommentMapper.countCoinsByCommentId(comment.getId());
                subCommentVO.setLikeNum(subLikeNum);
                subCommentVO.setCoinsNum(subCoinsNum);
                Boolean subLiked = false;
                Boolean subCoined = false;
                if (login) {
                    UserComment userComment = userCommentMapper.selectByUserIdAndCommentId(userId, subComment.getId());
                    if (userComment != null) {
                        subLiked = userComment.getLiked();
                        subCoined = userComment.getCoined();
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
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION,"没有权限");
        }
        int result1 = commentMapper.deleteByPrimaryKey(commentId);
        if (result1 != 1) {
            log.error("删除评论失败, userId:[{}], commentId:[{}]", userId, commentId);
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        // 删除userComment，所以用户被点赞数可能会减少
        UserComment userComment = userCommentMapper.selectByUserIdAndCommentId(userId, commentId);
        if (userComment != null) {
            userCommentMapper.deleteByPrimaryKey(userComment.getId());
        }
        return ResponseVO.success();
    }

    @Override
    public ResponseVO like(Integer commentId, String token) {
        return updateUserCommentState(commentId, token, UserContentStateEnum.LIKE, true);
    }

    @Override
    public ResponseVO dislike(Integer commentId, String token) {
        return updateUserCommentState(commentId, token, UserContentStateEnum.LIKE, false);
    }

    @Override
    public ResponseVO coin(Integer commentId, String token) {
        return updateUserCommentState(commentId, token, UserContentStateEnum.COIN, true);
    }

    ResponseVO updateUserCommentState(Integer commentId, String token, UserContentStateEnum stateEnum, Boolean state) {
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
        UserComment userComment = userCommentMapper.selectByUserIdAndCommentId(userId, commentId);
        if (userComment == null) {
            userComment = new UserComment();
            userComment.setUserId(userId);
            userComment.setCommentId(commentId);
        }
        if (UserContentStateEnum.LIKE.equals(stateEnum)) {
            userComment.setLiked(state);
        }else if (UserContentStateEnum.COIN.equals(stateEnum)) {
            // 不能给自己投币
            if (userId.equals(comment.getAuthorId())) {
                return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION, "不能给自己投币哦");
            }
            // 如果用户可投币数量小于1，提示硬币不足。
            User user = userMapper.selectByPrimaryKey(userId);
            if (user == null) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
            User author = userMapper.selectByPrimaryKey(comment.getAuthorId());
            if (user.getInsertableCoins() < 1) {
                return ResponseVO.error(ResponseEnum.COINS_NOT_ENOUGH);
            }
            if (state) {
                user.setInsertableCoins(user.getInsertableCoins() - 1);
                author.setExchangeableCoins(user.getExchangeableCoins() + 1);
                int result1 = userMapper.updateByPrimaryKeySelective(user);
                if (result1 != 1) {
                    return ResponseVO.error(ResponseEnum.ERROR);
                }
                int result2 = userMapper.updateByPrimaryKeySelective(author);
                if (result2 != 1) {
                    return ResponseVO.error(ResponseEnum.ERROR);
                }
            }
            userComment.setCoined(state);
        }else {
            return ResponseVO.error(ResponseEnum.ERROR,"更新帖子状态失败");
        }
        int result = userCommentMapper.updateState(userComment);
        if (result == 0 || result > 2) {
            log.error("更新帖子点赞/收藏/投币状态失败，数据库表'user_post'更新失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        return ResponseVO.success();
    }
}
