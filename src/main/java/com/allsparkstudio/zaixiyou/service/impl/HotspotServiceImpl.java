package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.enums.PostTypeEnum;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.po.*;
import com.allsparkstudio.zaixiyou.pojo.vo.*;
import com.allsparkstudio.zaixiyou.service.HotspotService;
import com.allsparkstudio.zaixiyou.util.DateUtils;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 热点相关
 *
 * @author wzr
 * @date 2023/7/18
 */
@Service
@Slf4j
public class HotspotServiceImpl implements HotspotService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private UserPostLikeMapper userPostLikeMapper;

    @Autowired
    private UserPostFavoriteMapper userPostFavoriteMapper;

    @Autowired
    private UserPostCoinMapper userPostCoinMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private HotspotPostMapper hotspotPostMapper;

    @Autowired
    private HotspotUserMapper hotspotUserMapper;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public ResponseVO<List<HotspotVO>> getCollection(String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }

        List<HotspotVO> hotspotVOS = new ArrayList<>();
        List<Topic> topics = topicMapper.selectAll();
        for (Topic topic : topics) {
            HotspotVO hotspotVO = new HotspotVO();
            hotspotVO.setTopicName(topic.getTopicName());

            List<Collection> collectionS = collectionMapper.selectByTopicId(topic.getId());
            List<CollectionVO> collectionVOS = new ArrayList<>();
            for (Collection collection : collectionS) {
                CollectionVO collectionVO = new CollectionVO();
                collectionVO.setCollectionId(collection.getId());
                collectionVO.setCollectionName(collection.getCollectionName());
                collectionVO.setCollectionMediaUrls(collection.getCollectionMediaUrls());

                collectionVOS.add(collectionVO);
            }
            hotspotVO.setCollectionNames(collectionVOS);

            hotspotVOS.add(hotspotVO);
        }
        return ResponseVO.success(hotspotVOS);
    }

    @Override
    public ResponseVO<List<HotspotPostVO>> getPostinfo(Integer collectionId, Integer pageNum, Integer pageSize, Integer sortedBy, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        List<HotspotPostVO> hotspotPostVOS = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        List<HotspotPost> hotspotPosts = hotspotPostMapper.selectByCollectionId(collectionId);

        if (hotspotPosts.isEmpty()) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR);
        }

        for (HotspotPost hotspotPost : hotspotPosts) {
            HotspotPostVO hotspotPostVO = new HotspotPostVO();

            Post post = postMapper.selectByPrimaryKey(hotspotPost.getPostId());

            User user = userMapper.selectByPrimaryKey(post.getAuthorId());
            hotspotPostVO.setId(user.getId());
            hotspotPostVO.setNickName(user.getNickname());
            hotspotPostVO.setAvatarUrl(user.getAvatarUrl());
            hotspotPostVO.setAccountAuth(Arrays.asList(user.getAccountAuth().split(";")));
            hotspotPostVO.setLevel(user.getLevel());

            hotspotPostVO.setCreateTime(DateUtils.formatPublishTime(post.getCreateTime()));

            Integer likesNum = userPostLikeMapper.countByPostId(post.getId());
            Integer favoritesNum = userPostFavoriteMapper.countByPostId(post.getId());
            Integer commentsNum = commentMapper.countCommentsByPostId(post.getId());
            hotspotPostVO.setLikeNum(likesNum);
            hotspotPostVO.setFavoriteNum(favoritesNum);
            hotspotPostVO.setCommentNum(commentsNum);

            Integer myId = jwtUtils.getIdFromToken(token);
            boolean liked = false;
            boolean favorited = false;
            boolean forwarded = false;
            UserPostLike userPostLike = userPostLikeMapper.selectByUserIdAndPostId(myId, post.getId());
            UserPostFavorite userPostFavorite = userPostFavoriteMapper.selectByUserIdAndPostId(myId, post.getId());
            if (userPostLike != null && userPostLike.getState().equals(1)) {
                liked = true;
            }
            if (userPostFavorite != null && userPostFavorite.getState().equals(1)) {
                favorited = true;
            }
            // TODO: 转发模块
            hotspotPostVO.setLiked(liked);
            hotspotPostVO.setFavorited(favorited);
            hotspotPostVO.setForwarded(forwarded);

            String body = null;
            if (post.getBody().length() > 30) {
                body = post.getBody().substring(0, 30);
            }else {
                body = post.getBody();
            }
            hotspotPostVO.setPostBody(body);
            hotspotPostVO.setPostMediaUrls(post.getPostMediaUrls());

            hotspotPostVOS.add(hotspotPostVO);
        }
        return ResponseVO.success(hotspotPostVOS);
    }

    @Override
    public ResponseVO<List<HotspotUserVO>> getUserinfo(Integer pageNum, Integer pageSize, Integer sortedBy, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        List<HotspotUserVO> hotspotUserVOS = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        List<HotspotUser> hotspotUsers = hotspotUserMapper.selectAll();

        if (hotspotUsers.isEmpty()) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR);
        }

        for (HotspotUser hotspotUser : hotspotUsers) {
            HotspotUserVO hotspotUserVO = new HotspotUserVO();

            User user = userMapper.selectByPrimaryKey(hotspotUser.getUserId());
            hotspotUserVO.setId(user.getId());
            hotspotUserVO.setNickName(user.getNickname());
            hotspotUserVO.setAvatarUrl(user.getAvatarUrl());
            hotspotUserVO.setAccountAuth(Arrays.asList(user.getAccountAuth().split(";")));
            hotspotUserVO.setLevel(user.getLevel());

            Integer fansNum = followMapper.countByFollowedUserId(user.getId());
            hotspotUserVO.setFansNum(fansNum);
            Integer postNum = postMapper.countByAuthorIdAndType(user.getId(), PostTypeEnum.POST.getCode());
            hotspotUserVO.setPostNum(postNum);
            hotspotUserVO.setLikeNum(user.getLikeNum());

            boolean isFollowed = false;
            Integer myId = jwtUtils.getIdFromToken(token);
            isFollowed = followMapper.isFollowed(myId, user.getId());
            hotspotUserVO.setFollowed(isFollowed);

            hotspotUserVOS.add(hotspotUserVO);
        }

        return ResponseVO.success(hotspotUserVOS);
    }
}
