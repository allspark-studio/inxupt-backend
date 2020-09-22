package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.enums.*;
import com.allsparkstudio.zaixiyou.pojo.form.AddArticleForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddCircleArticleForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddCirclePostForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddPostForm;
import com.allsparkstudio.zaixiyou.pojo.po.*;
import com.allsparkstudio.zaixiyou.pojo.vo.PostVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.PostService;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import com.allsparkstudio.zaixiyou.util.UserDailyStatisticsUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author 陈帅
 * @date 2020/8/20
 */
@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPostMapper userPostMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostCategoryMapper postCategoryMapper;

    @Autowired
    private UserCircleMapper userCircleMapper;

    @Autowired
    private PostCircleMapper postCircleMapper;

    @Autowired
    private UserPostLikeMapper userPostLikeMapper;

    @Autowired
    private UserPostFavoriteMapper userPostFavoriteMapper;

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    UserDailyStatisticsUtils userDailyStatisticsUtils;

    @Override
    public ResponseVO<PageInfo> listAll(Integer categoryId, Integer userId, Integer circleId,
                                        Integer type, UserContentStateEnum stateEnum,
                                        String token, Integer pageNum, Integer pageSize,
                                        Integer sortedBy) {
        Integer myId = null;
        boolean login = false;
        if (!StringUtils.isEmpty(token)) {
            if (jwtUtils.validateToken(token)) {
                myId = jwtUtils.getIdFromToken(token);
                login = true;
            }
        }
        List<Post> postList;
        if (categoryId != null) {
            // 根据分类来列出帖子
            if (SortTypeEnum.HEAT.getCode().equals(sortedBy)) {
                PageHelper.startPage(pageNum, pageSize);
                postList = postMapper.selectPostsByCategoryIdSortedByHeat(categoryId);
            } else {
                PageHelper.startPage(pageNum, pageSize);
                postList = postMapper.selectPostsByCategoryIdSortedByTime(categoryId);
            }
        } else if (userId != null && type != null) {
            // 根据作者和帖子类型来列出帖子
            PageHelper.startPage(pageNum, pageSize);
            postList = postMapper.selectByUserIdAndType(userId, type);
        } else if (stateEnum == UserContentStateEnum.FAVORITE && type != null) {
            // 根据用户点赞/投币/收藏来列出帖子
            if (!login) {
                return ResponseVO.error(ResponseEnum.NEED_LOGIN);
            }
            PageHelper.startPage(pageNum, pageSize);
            postList = postMapper.selectFavoritesPostsByUserIdAndType(myId, type);
        } else if (circleId != null) {
            // 根据圈子来列出帖子
            if (SortTypeEnum.HEAT.getCode().equals(sortedBy)) {
                PageHelper.startPage(pageNum, pageSize);
                postList = postMapper.selectPostsByCircleIdSortedByHeat(circleId);
            } else {
                PageHelper.startPage(pageNum, pageSize);
                postList = postMapper.selectPostsByCircleIdSortedByTime(circleId);
            }
        } else {
            // 列出全部帖子
            if (SortTypeEnum.HEAT.getCode().equals(sortedBy)) {
                PageHelper.startPage(pageNum, pageSize);
                postList = postMapper.selectAllByHeat();
            }else {
                PageHelper.startPage(pageNum, pageSize);
                postList = postMapper.selectAllByTime();
            }
        }
        List<PostVO> postVOList = new ArrayList<>();
        for (Post post : postList) {
            Integer postType = post.getType();
            PostVO postVO = new PostVO();
            postVO.setType(postType);
            if (PostTypeEnum.POST.getCode().equals(postType)) {
                if (post.getPostMediaUrls() != null && !StringUtils.isEmpty(post.getPostMediaUrls())) {
                    List<String> mediaUrlList;
                    String[] mediaUrl = post.getPostMediaUrls().split(";");
                    mediaUrlList = Arrays.asList(mediaUrl);
                    postVO.setMediaUrls(mediaUrlList);
                }
            }
            if (PostTypeEnum.ARTICLE.getCode().equals(postType)) {
                postVO.setTitle(post.getArticleTitle());
                postVO.setPureText(post.getArticleText());
                postVO.setCover(post.getArticleCover());
            }
            postVO.setPostId(post.getId());
            User author = userMapper.selectByPrimaryKey(post.getAuthorId());
            postVO.setAuthorId(author.getId());
            postVO.setAuthorName(author.getNickname());
            postVO.setAuthorAvatar(author.getAvatarUrl());
            postVO.setAuthorDescription(author.getDescription());
            postVO.setAuthorLevel(author.getLevel());
            postVO.setBody(post.getBody());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            postVO.setCreateTime(simpleDateFormat.format(post.getCreateTime()));
            Integer likesNum = userPostMapper.countLikeByPostId(post.getId());
            Integer favoritesNum = userPostMapper.countFavoriteByPostId(post.getId());
            Integer coinsNum = userPostMapper.countCoinsByPostId(post.getId());
            Integer commentsNum = commentMapper.countCommentsByPostId(post.getId());
            postVO.setLikeNum(likesNum);
            postVO.setFavoriteNum(favoritesNum);
            postVO.setCoinsNum(coinsNum);
            postVO.setCommentNum(commentsNum);
            Boolean liked = false;
            Boolean coined = false;
            Boolean favorited = false;
            if (login) {
                UserPost userPost = userPostMapper.selectByUserIdAndPostId(myId, post.getId());
                if (userPost != null) {
                    liked = userPost.getLiked();
                    coined = userPost.getCoined();
                    favorited = userPost.getFavorited();
                }
            }
            postVO.setLiked(liked);
            postVO.setCoined(coined);
            postVO.setFavorited(favorited);

            // 设置@的用户
            if (post.getAtIds() != null && !StringUtils.isEmpty(post.getAtIds())) {
                List<Map<String, Object>> atMapList = new ArrayList<>();
                String[] atIds = post.getAtIds().split(";");
                for (String atId : atIds) {
                    User user = userMapper.selectByPrimaryKey(Integer.parseInt(atId));
                    Map<String, Object> atMap = new HashMap<>(2);
                    atMap.put("id", user.getId());
                    atMap.put("nickName", user.getNickname());
                    atMapList.add(atMap);
                }
                postVO.setAts(atMapList);
            }

            // 设置自定义标签
            if (post.getTags() != null && !StringUtils.isEmpty(post.getTags())) {
                List<String> tagList;
                String[] tags = post.getTags().split(";");
                tagList = Arrays.asList(tags);
                // 历史遗留原因导致要用map存
                List<Map<String, String>> tagMapList = new ArrayList<>();
                for (String tag : tagList) {
                    Map<String, String> tagMap = new HashMap<>(2);
                    tagMap.put("name", tag);
                    tagMapList.add(tagMap);
                }
                postVO.setTags(tagMapList);
            }
            postVOList.add(postVO);
        }
        PageInfo pageInfo = new PageInfo<>(postList);
        pageInfo.setList(postVOList);
        return ResponseVO.success(pageInfo);
    }

    @Override
    public ResponseVO like(Integer postId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post == null) {
            log.error("请求的帖子不存在, postId:[{}]", postId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "帖子不存在");
        }
        UserPostLike userPostLike = userPostLikeMapper.selectByUserIdAndPostId(userId, postId);
        if (userPostLike == null) {
            userPostLike = new UserPostLike();
            userPostLike.setUserId(userId);
            userPostLike.setPostId(postId);
            userPostLike.setState(1);
            int result = userPostLikeMapper.insertSelective(userPostLike);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
            // MQ增加经验值
            rabbitTemplate.convertAndSend("dailyStatisticsExchange", "getLike", post.getAuthorId());
            // TODO: MQ通知作者内容被点赞
        }else {
            userPostLike.setState(1);
            int result = userPostLikeMapper.updateByPrimaryKeySelective(userPostLike);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        return ResponseVO.success();
    }

    @Override
    public ResponseVO dislike(Integer postId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post == null) {
            log.error("请求的帖子不存在, postId:[{}]", postId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "帖子不存在");
        }
        UserPostLike userPostLike = userPostLikeMapper.selectByUserIdAndPostId(userId, postId);
        if (userPostLike == null) {
            userPostLike = new UserPostLike();
            userPostLike.setUserId(userId);
            userPostLike.setPostId(postId);
            userPostLike.setState(0);
            int result = userPostLikeMapper.insertSelective(userPostLike);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }else {
            userPostLike.setState(0);
            int result = userPostLikeMapper.updateByPrimaryKeySelective(userPostLike);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        return ResponseVO.success();
    }

    @Override
    public ResponseVO favorite(Integer postId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post == null) {
            log.error("请求的帖子不存在, postId:[{}]", postId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "帖子不存在");
        }
        UserPostFavorite userPostFavorite = userPostFavoriteMapper.selectByUserIdAndPostId(userId, postId);
        if (userPostFavorite == null) {
            userPostFavorite = new UserPostFavorite();
            userPostFavorite.setUserId(userId);
            userPostFavorite.setPostId(postId);
            userPostFavorite.setState(1);
            int result = userPostFavoriteMapper.insertSelective(userPostFavorite);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
            // MQ增加经验值
            rabbitTemplate.convertAndSend("dailyStatisticsExchange", "getFavorite", post.getAuthorId());
            // TODO: MQ通知作者内容被收藏
        }else {
            userPostFavorite.setState(1);
            int result = userPostLikeMapper.updateByPrimaryKeySelective(userPostFavorite);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        return ResponseVO.success();
    }

    @Override
    public ResponseVO disFavorite(Integer postId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post == null) {
            log.error("请求的帖子不存在, postId:[{}]", postId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "帖子不存在");
        }
        UserPostFavorite userPostFavorite = userPostFavoriteMapper.selectByUserIdAndPostId(userId, postId);
        if (userPostFavorite == null) {
            userPostFavorite = new UserPostFavorite();
            userPostFavorite.setUserId(userId);
            userPostFavorite.setPostId(postId);
            userPostFavorite.setState(0);
            int result = userPostFavoriteMapper.insertSelective(userPostFavorite);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }else {
            userPostFavorite.setState(0);
            int result = userPostLikeMapper.updateByPrimaryKeySelective(userPostFavorite);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        return ResponseVO.success();
    }

    @Override
    public ResponseVO coin(Integer postId, String token) {
        return updateUserPostState(postId, token, UserContentStateEnum.COIN, true);
    }

    @Override
    public ResponseVO<Map<String, Integer>> addPost(AddPostForm addPostForm, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        User user = userMapper.selectByPrimaryKey(userId);
        if (UserStateEnum.DISABLE_SEND_MESSAGE.getCode().equals(user.getState())) {
            return ResponseVO.error(ResponseEnum.DISABLE_SEND_MESSAGE);
        }
        if (userDailyStatisticsUtils.isAddPostLimited(userId)) {
            return ResponseVO.error(ResponseEnum.REACH_PUBLISH_LIMIT);
        }
        if ((addPostForm.getBody() == null || StringUtils.isEmpty(addPostForm.getBody())) &&
                addPostForm.getMediaUrls() == null || StringUtils.isEmpty(addPostForm.getMediaUrls())) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "请填写内容");
        }
        Post post = new Post();
        post.setType(PostTypeEnum.POST.getCode());
        post.setAuthorId(userId);
        post.setBody(addPostForm.getBody());
        // 设置帖子的图片或视频url列表
        if (addPostForm.getMediaUrls() != null && addPostForm.getMediaUrls().size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String mediaUrl : addPostForm.getMediaUrls()) {
                stringBuilder.append(mediaUrl);
                stringBuilder.append(";");
            }
            String postMediaUrls = stringBuilder.substring(0, stringBuilder.length() - 1);
            post.setPostMediaUrls(postMediaUrls);
        }
        // 设置文章@的人的id
        if (addPostForm.getAtIds() != null && addPostForm.getAtIds().size() != 0) {
            StringBuilder stringBuilder1 = new StringBuilder();
            for (Integer atId : addPostForm.getAtIds()) {
                stringBuilder1.append(atId);
                stringBuilder1.append(";");
            }
            String atIds = stringBuilder1.substring(0, stringBuilder1.length() - 1);
            post.setAtIds(atIds);
        }
        // 设置帖子的自定义标签
        if (addPostForm.getCustomTags() != null && addPostForm.getCustomTags().size() != 0) {
            StringBuilder stringBuilder1 = new StringBuilder();
            for (String tag : addPostForm.getCustomTags()) {
                stringBuilder1.append(tag);
                stringBuilder1.append(";");
            }
            String tags = stringBuilder1.substring(0, stringBuilder1.length() - 1);
            post.setTags(tags);
        }
        int result = postMapper.insertSelective(post);
        if (result != 1) {
            log.error("新建文章时，数据库表'post'插入失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        // MQ更新用户当日发帖子数量，更新用户经验
        rabbitTemplate.convertAndSend("dailyStatisticsExchange", "addPost", userId);
        // 通过MQ同步数据到ES
        rabbitTemplate.convertAndSend("MySQL2ESPostExchange", "add", post);
        // 绑定主标签（分类）和帖子
        for (Integer mainTagId : addPostForm.getMainTagIds()) {
            PostCategory postCategory = new PostCategory();
            postCategory.setPostId(post.getId());
            postCategory.setCategoryId(mainTagId);
            int result1 = postCategoryMapper.insertSelective(postCategory);
            if (result1 != 1) {
                log.error("新建文章时，数据库表'post_category'插入失败");
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        // 设置文章同步到哪个圈子
        for (Integer circleId : addPostForm.getCircleIds()) {
            PostCircle postCircle = new PostCircle();
            postCircle.setPostId(post.getId());
            postCircle.setCircleId(circleId);
            int result2 = postCircleMapper.insertSelective(postCircle);
            if (result2 != 1) {
                log.error("新建文章时，数据库表'post_circle'插入失败");
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        Map<String, Integer> map = new HashMap<>(1);
        map.put("postId", post.getId());
        return ResponseVO.success(map);
    }

    @Override
    public ResponseVO<Map<String, Integer>> addArticle(AddArticleForm addArticleForm, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        User user = userMapper.selectByPrimaryKey(userId);
        if (UserStateEnum.DISABLE_SEND_MESSAGE.getCode().equals(user.getState())) {
            return ResponseVO.error(ResponseEnum.DISABLE_SEND_MESSAGE);
        }
        if (userDailyStatisticsUtils.isAddPostLimited(userId)) {
            return ResponseVO.error(ResponseEnum.REACH_PUBLISH_LIMIT);
        }
        if (addArticleForm.getTitle() == null || StringUtils.isEmpty(addArticleForm.getTitle())) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "请输入标题");
        }
        Post article = new Post();
        article.setType(PostTypeEnum.ARTICLE.getCode());
        article.setArticleCover(addArticleForm.getCover());
        article.setArticleText(addArticleForm.getPureText());
        article.setAuthorId(userId);
        article.setArticleTitle(addArticleForm.getTitle());
        article.setBody(addArticleForm.getBody());
        // 设置文章@的人的id
        if (addArticleForm.getAtIds() != null && addArticleForm.getAtIds().size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Integer atId : addArticleForm.getAtIds()) {
                stringBuilder.append(atId);
                stringBuilder.append(";");
            }
            String atIds = stringBuilder.substring(0, stringBuilder.length() - 1);
            article.setAtIds(atIds);
        }
        // 设置文章的自定义标签
        if (addArticleForm.getCustomTags() != null && addArticleForm.getCustomTags().size() != 0) {
            StringBuilder stringBuilder1 = new StringBuilder();
            for (String tag : addArticleForm.getCustomTags()) {
                stringBuilder1.append(tag);
                stringBuilder1.append(";");
            }
            String tags = stringBuilder1.substring(0, stringBuilder1.length() - 1);
            article.setTags(tags);
        }
        int result = postMapper.insertSelective(article);
        if (result != 1) {
            log.error("新建文章时，数据库表'post'插入失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        // MQ更新用户当日发帖子数量，更新用户经验
        rabbitTemplate.convertAndSend("dailyStatisticsExchange", "addPost", userId);
        // 通过MQ同步数据到ES
        rabbitTemplate.convertAndSend("MySQL2ESPostExchange", "add", article);
        // 绑定主标签（分类）和帖子
        for (Integer mainTagId : addArticleForm.getMainTagIds()) {
            PostCategory postCategory = new PostCategory();
            postCategory.setPostId(article.getId());
            postCategory.setCategoryId(mainTagId);
            int result1 = postCategoryMapper.insertSelective(postCategory);
            if (result1 != 1) {
                log.error("新建文章时，数据库表'post_category'插入失败");
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        // 设置文章同步到哪个圈子
        for (Integer circleId : addArticleForm.getCircleIds()) {
            PostCircle postCircle = new PostCircle();
            postCircle.setPostId(article.getId());
            postCircle.setCircleId(circleId);
            int result2 = postCircleMapper.insertSelective(postCircle);
            if (result2 != 1) {
                log.error("新建文章时，数据库表'post_circle'插入失败");
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        Map<String, Integer> map = new HashMap<>(1);
        map.put("postId", article.getId());
        return ResponseVO.success(map);
    }

    @Override
    public ResponseVO<Map<String, Integer>> addPostInCircle(Integer circleId, AddCirclePostForm form, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        User user = userMapper.selectByPrimaryKey(userId);
        if (UserStateEnum.DISABLE_SEND_MESSAGE.getCode().equals(user.getState())) {
            return ResponseVO.error(ResponseEnum.DISABLE_SEND_MESSAGE);
        }
        if (userDailyStatisticsUtils.isAddPostLimited(userId)) {
            return ResponseVO.error(ResponseEnum.REACH_PUBLISH_LIMIT);
        }
        Integer role = userCircleMapper.selectRoleOrNull(userId, circleId);
        if (role == null || role < UserCircleRoleEnum.FOLLOW.getCode()) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION);
        }
        Post post = new Post();
        post.setType(PostTypeEnum.POST.getCode());
        post.setAuthorId(userId);
        post.setBody(form.getBody());
        post.setVisibleOutsideCircle(false);
        if (form.getMediaUrls() != null && form.getMediaUrls().size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String mediaUrl : form.getMediaUrls()) {
                stringBuilder.append(mediaUrl);
                stringBuilder.append(";");
            }
            String postMediaUrls = stringBuilder.substring(0, stringBuilder.length() - 1);
            post.setPostMediaUrls(postMediaUrls);
        }
        int result = postMapper.insertSelective(post);
        if (result != 1) {
            log.error("圈子内发布帖子时出现错误,数据库表'post'插入失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        // MQ更新用户当日发帖子数量，更新用户经验
        rabbitTemplate.convertAndSend("dailyStatisticsExchange", "addPost", userId);
        // 通过MQ同步数据到ES
        rabbitTemplate.convertAndSend("MySQL2ESPostExchange", "add", post);
        PostCircle postCircle = new PostCircle();
        postCircle.setCircleId(circleId);
        postCircle.setPostId(post.getId());
        int result1 = postCircleMapper.insertSelective(postCircle);
        if (result1 != 1) {
            log.error("圈子内发布帖子时出现错误,数据库表'post_circle'插入失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        Map<String, Integer> map = new HashMap<>(1);
        map.put("postId", post.getId());
        return ResponseVO.success(map);
    }

    @Override
    public ResponseVO<Map<String, Integer>> addArticleInCircle(Integer circleId, AddCircleArticleForm form, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        User user = userMapper.selectByPrimaryKey(userId);
        if (UserStateEnum.DISABLE_SEND_MESSAGE.getCode().equals(user.getState())) {
            return ResponseVO.error(ResponseEnum.DISABLE_SEND_MESSAGE);
        }
        if (userDailyStatisticsUtils.isAddPostLimited(userId)) {
            return ResponseVO.error(ResponseEnum.REACH_PUBLISH_LIMIT);
        }
        Integer role = userCircleMapper.selectRoleOrNull(userId, circleId);
        if (role == null || role < UserCircleRoleEnum.FOLLOW.getCode()) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION);
        }
        Post article = new Post();
        article.setType(PostTypeEnum.ARTICLE.getCode());
        article.setArticleText(form.getPureText());
        article.setArticleCover(form.getCover());
        article.setAuthorId(userId);
        article.setBody(form.getBody());
        article.setVisibleOutsideCircle(false);
        article.setArticleTitle(form.getTitle());
        int result = postMapper.insertSelective(article);
        if (result != 1) {
            log.error("圈子内发布文章时出现错误,数据库表'post'插入失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        // MQ更新用户当日发帖子数量，更新用户经验
        rabbitTemplate.convertAndSend("dailyStatisticsExchange", "addPost", userId);
        // 通过MQ同步数据到ES
        rabbitTemplate.convertAndSend("MySQL2ESPostExchange", "add", article);
        PostCircle postCircle = new PostCircle();
        postCircle.setCircleId(circleId);
        postCircle.setPostId(article.getId());
        int result1 = postCircleMapper.insertSelective(postCircle);
        if (result1 != 1) {
            log.error("圈子内发布文章时出现错误,数据库表'post_circle'插入失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        Map<String, Integer> map = new HashMap<>(1);
        map.put("postId", article.getId());
        return ResponseVO.success(map);
    }

    @Override
    public ResponseVO<PostVO> getPost(Integer postId, String token) {
        Integer myId = null;
        boolean login = false;
        if (!StringUtils.isEmpty(token)) {
            if (jwtUtils.validateToken(token)) {
                myId = jwtUtils.getIdFromToken(token);
                login = true;
            }
        }
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post == null) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "帖子不存在");
        }
        Integer postType = post.getType();
        PostVO postVO = new PostVO();
        postVO.setType(postType);
        if (PostTypeEnum.POST.getCode().equals(postType)) {
            if (post.getPostMediaUrls() != null && !StringUtils.isEmpty(post.getPostMediaUrls())) {
                List<String> mediaUrlList;
                String[] mediaUrl = post.getPostMediaUrls().split(";");
                mediaUrlList = Arrays.asList(mediaUrl);
                postVO.setMediaUrls(mediaUrlList);
            }
        }
        if (PostTypeEnum.ARTICLE.getCode().equals(postType)) {
            postVO.setTitle(post.getArticleTitle());
            postVO.setPureText(post.getArticleText());
            postVO.setCover(post.getArticleCover());
        }
        postVO.setPostId(post.getId());
        User author = userMapper.selectByPrimaryKey(post.getAuthorId());
        postVO.setAuthorId(author.getId());
        postVO.setAuthorName(author.getNickname());
        postVO.setAuthorAvatar(author.getAvatarUrl());
        postVO.setAuthorDescription(author.getDescription());
        postVO.setAuthorLevel(author.getLevel());
        postVO.setBody(post.getBody());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        postVO.setCreateTime(simpleDateFormat.format(post.getCreateTime()));
        Integer likesNum = userPostMapper.countLikeByPostId(postId);
        Integer favoritesNum = userPostMapper.countFavoriteByPostId(postId);
        Integer coinsNum = userPostMapper.countCoinsByPostId(postId);
        Integer commentsNum = commentMapper.countCommentsByPostId(postId);
        postVO.setLikeNum(likesNum);
        postVO.setFavoriteNum(favoritesNum);
        postVO.setCoinsNum(coinsNum);
        postVO.setCommentNum(commentsNum);
        Boolean liked = false;
        Boolean coined = false;
        Boolean favorited = false;
        if (login) {
            UserPost userPost = userPostMapper.selectByUserIdAndPostId(myId, post.getId());
            if (userPost != null) {
                liked = userPost.getLiked();
                coined = userPost.getCoined();
                favorited = userPost.getFavorited();
            }
        }
        postVO.setLiked(liked);
        postVO.setCoined(coined);
        postVO.setFavorited(favorited);

        // 设置@的用户列表
        if (post.getAtIds() != null && !StringUtils.isEmpty(post.getAtIds())) {
            List<Map<String, Object>> atMapList = new ArrayList<>();
            String[] atIds = post.getAtIds().split(";");
            for (String atId : atIds) {
                User user = userMapper.selectByPrimaryKey(Integer.parseInt(atId));
                Map<String, Object> atMap = new HashMap<>(2);
                atMap.put("id", user.getId());
                atMap.put("nickName", user.getNickname());
                atMapList.add(atMap);
            }
            postVO.setAts(atMapList);
        }

        // 设置自定义标签
        if (post.getTags() != null && !StringUtils.isEmpty(post.getTags())) {
            List<String> tagList;
            String[] tags = post.getTags().split(";");
            tagList = Arrays.asList(tags);
            // 历史遗留原因导致要用map存
            List<Map<String, String>> tagMapList = new ArrayList<>();
            for (String tag : tagList) {
                Map<String, String> tagMap = new HashMap<>(2);
                tagMap.put("name", tag);
                tagMapList.add(tagMap);
            }
            postVO.setTags(tagMapList);
        }
        return ResponseVO.success(postVO);
    }

    @Override
    public ResponseVO deletePost(Integer postId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post == null) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "帖子不存在");
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        if (!post.getAuthorId().equals(userId)) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION);
        }
        // 删除帖子
        postMapper.deleteByPrimaryKey(postId);
        // 通过MQ同步删除ES数据
        rabbitTemplate.convertAndSend("MySQL2ESPostExchange", "delete", post);
        // 删除帖子下面的评论，但不删除用户-评论关系表，这样用户被点赞数不会减只会加
//        List<Integer> commentIdList = commentMapper.selectIdsByPostId(postId);
//        for (Integer commentId : commentIdList) {
//            userCommentMapper.deleteByCommentId(commentId);
//        }
        commentMapper.deleteByPostId(postId);
        // 不删除用户-帖子关联动作，这样用户被点赞数不会减只会加
        userPostMapper.deleteByPostId(postId);
        // 删除帖子和分类的关系
        postCategoryMapper.deleteByPostId(postId);
        // 删除帖子和圈子的关系
        postCircleMapper.deleteByPostId(postId);
        return ResponseVO.success();
    }

    @Transactional
    ResponseVO updateUserPostState(Integer postId, String token, UserContentStateEnum stateEnum, Boolean state) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post == null) {
            log.error("请求的帖子不存在, postId:[{}]", postId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "帖子不存在");
        }
        UserPost userPost = userPostMapper.selectByUserIdAndPostId(userId, postId);
        if (userPost == null) {
            userPost = new UserPost();
            userPost.setUserId(userId);
            userPost.setPostId(postId);
        }
        if (UserContentStateEnum.LIKE.equals(stateEnum)) {
            userPost.setLiked(state);
        } else if (UserContentStateEnum.FAVORITE.equals(stateEnum)) {
            userPost.setFavorited(state);
        } else if (UserContentStateEnum.COIN.equals(stateEnum)) {
            // 不能给自己投币
            if (userId.equals(post.getAuthorId())) {
                return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION, "不能给自己投币哦");
            }
            // 如果用户可投币数量小于1，提示硬币不足。
            User user = userMapper.selectByPrimaryKey(userId);
            if (user == null) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
            User author = userMapper.selectByPrimaryKey(post.getAuthorId());
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
            userPost.setCoined(state);
        } else {
            return ResponseVO.error(ResponseEnum.ERROR, "更新帖子状态失败");
        }
        int result = userPostMapper.updateState(userPost);
        if (result == 0 || result > 2) {
            log.error("更新帖子点赞/收藏/投币状态失败，数据库表'user_post'更新失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        // MQ更新经验
        if (UserContentStateEnum.LIKE.equals(stateEnum) && state) {
            rabbitTemplate.convertAndSend("dailyStatisticsExchange", "getLike", post.getAuthorId());
        } else if (UserContentStateEnum.FAVORITE.equals(stateEnum) && state) {
            rabbitTemplate.convertAndSend("dailyStatisticsExchange", "getFavorite", post.getAuthorId());
        } else if (UserContentStateEnum.COIN.equals(stateEnum) && state) {
            rabbitTemplate.convertAndSend("dailyStatisticsExchange", "insertCoin", userId);
            rabbitTemplate.convertAndSend("dailyStatisticsExchange", "getCoin", post.getAuthorId());
        }
        return ResponseVO.success();
    }
}
