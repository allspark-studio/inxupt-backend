package com.allsparkstudio.zaixiyou.controller;


import com.allsparkstudio.zaixiyou.consts.ExperienceConst;
import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.enums.PostTypeEnum;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.ESEntity.ESCircle;
import com.allsparkstudio.zaixiyou.pojo.ESEntity.ESPost;
import com.allsparkstudio.zaixiyou.pojo.ESEntity.ESUser;
import com.allsparkstudio.zaixiyou.pojo.po.Circle;
import com.allsparkstudio.zaixiyou.pojo.po.Comment;
import com.allsparkstudio.zaixiyou.pojo.po.Post;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;

/**
 * 第三方调用的Api接口
 * 这个只是暂时这么用，所以没有分三层架构
 * 如果要更进一步完善功能，一定要把业务下放到service层
 * @author AlkaidChen
 */
@Api(tags = "后台接口自定义调用")
@RestController
@Slf4j
public class ApiOperation {

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Autowired
    UserService userService;

    @Autowired
    PostMapper postMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CircleMapper circleMapper;

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
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;


    @PostMapping("/api/syncES")
    @io.swagger.annotations.ApiOperation("重新同步全部MySQL数据到ES")
    public ResponseVO syncAll(String password) throws IOException {
        if (!"allspark520".equals(password)) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION);
        }
        // 删除并重置索引
        deleteIndex("post");
        createIndex("post");
        // 同步全部post
        List<Post> postList = postMapper.selectAllByTime();
        for (Post post : postList) {
            syncPost(post);
        }

        // 删除并重置索引
        deleteIndex("user");
        createIndex("user");
        List<User> userList = userMapper.selectAll();
        // 同步全部user
        for (User user : userList) {
            syncUser(user);
        }

        // 删除并重置索引
        deleteIndex("circle");
        createIndex("circle");
        List<Circle> circleList = circleMapper.selectAll();
        // 同步全部circle
        for (Circle circle : circleList) {
            syncCircle(circle);
        }
        return ResponseVO.success();
    }

    /**
     * 同步增加帖子数据到ES中
     */
    private void syncPost(Post post) throws IOException {
        IndexRequest request = new IndexRequest("post");
        request.id(String.valueOf(post.getId()));
        ESPost esPost = new ESPost();
        esPost.setItemId(post.getId());
        esPost.setType(post.getType());
        if (PostTypeEnum.POST.getCode().equals(post.getType())) {
            esPost.setBody(post.getBody());
        }else {
            esPost.setTitle(post.getArticleTitle());
            esPost.setPureText(post.getArticleText());
        }
        request.source(gson.toJson(esPost), XContentType.JSON);

        client.index(request, RequestOptions.DEFAULT);
    }

    /**
     * 同步增加用户数据到ES中
     */
    private void syncUser(User user) throws IOException {
        IndexRequest request = new IndexRequest("user");
        request.id(String.valueOf(user.getId()));
        ESUser esUser = new ESUser();
        esUser.setItemId(user.getId());
        esUser.setName(user.getNickname());
        request.source(gson.toJson(esUser), XContentType.JSON);

        client.index(request, RequestOptions.DEFAULT);
    }

    /**
     * 同步增加圈子数据到ES中
     */
    private void syncCircle(Circle circle) throws IOException {
        IndexRequest request = new IndexRequest("circle");
        request.id(String.valueOf(circle.getId()));
        ESCircle esCircle = new ESCircle();
        esCircle.setItemId(circle.getId());
        esCircle.setName(circle.getName());
        request.source(gson.toJson(esCircle), XContentType.JSON);

        client.index(request, RequestOptions.DEFAULT);
    }

    /**
     * 新建索引
     * @param indexName 索引名字
     */
    private void createIndex(String indexName) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * 删除索引
     * @param indexName 索引名字
     */
    private void deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    /**
     * 更新帖子热度
     */
    @PostMapping("/api/post/heat")
    @io.swagger.annotations.ApiOperation("重新计算全部帖子热度")
    public ResponseVO updatePostHeat(String password) {
        if (!"allspark520".equals(password)) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION);
        }
        for (int i = 0; i < 100000; i++) {
            Post post = postMapper.selectByPrimaryKey(i);
            if (post == null) {
                continue;
            }
            calculatePostHeat(i);
        }

        return ResponseVO.success();
    }

    /**
     * 更新评论热度
     */
    @PostMapping("/api/comment/heat")
    @io.swagger.annotations.ApiOperation("重新计算全部评论热度")
    public ResponseVO updateCommentHeat(String password) {
        if (!"allspark520".equals(password)) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION);
        }
        for (int i = 0; i < 100000; i++) {
            Comment comment = commentMapper.selectByPrimaryKey(i);
            if (comment == null) {
                continue;
            }
            calculateCommentHeat(i);
        }
        return ResponseVO.success();
    }

    private void calculatePostHeat(Integer postId) {
        int likesNum = userPostLikeMapper.countByPostId(postId);
        int coinsNum = userPostFavoriteMapper.countByPostId(postId);
        int favoritesNum = userPostCoinMapper.countByPostId(postId);
        int commentsNum = commentMapper.countCommentsByPostId(postId);
        Post post = postMapper.selectByPrimaryKey(postId);
        int hour = (int) ((System.currentTimeMillis() - post.getCreateTime().getTime()) / (1000 * 60 * 60));
        Integer heat = (likesNum * 10 + coinsNum * 20 + favoritesNum * 20 + commentsNum * 30) / (int) Math.pow(hour + 36, 1.1);
        post.setHeat(heat);
        postMapper.updateHeat(post);
    }

    private void calculateCommentHeat(Integer commentId) {
        int likesNum = userCommentLikeMapper.countByCommentId(commentId);
        int coinsNum = userCommentCoinMapper.countByCommentId(commentId);
        int commentsNum = commentMapper.countSubCommentsByCommentId(commentId);
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        int hour = (int) ((System.currentTimeMillis() - comment.getCreateTime().getTime()) / (1000 * 60 * 60));
        Integer heat = (likesNum * 10 + coinsNum * 20 + commentsNum * 30) / (int) Math.pow(hour + 36, 1.1);
        comment.setHeat(heat);
        commentMapper.updateHeat(comment);
    }

    /**
     * 更新评论热度
     */
    @PutMapping("/api/user/exp")
    @io.swagger.annotations.ApiOperation("给用户加经验")
    public ResponseVO addExperience(String password,
                                    @RequestParam("phone") String phone,
                                    @RequestParam("experience") Integer exp) {
        if (!"allspark520".equals(password)) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION);
        }
        User user = userMapper.selectByPhone(phone);
        if (user == null) {
            return ResponseVO.error(ResponseEnum.ERROR, "用户不存在");
        }
        user.setExperience(user.getExperience() + exp);
        while (user.getExperience() >= ExperienceConst.getExpByLv(user.getLevel())) {
            user.setLevel(user.getLevel() + 1);
        }
        userMapper.updateExpAndLv(user);
        log.info("用户[{}],id[{}]，通过后台api增加经验：[{}]", user.getNickname(),user.getId(), exp);
        RestTemplate restTemplate = new RestTemplate();
        // 请求地址
        String url = "http://154.8.202.244:8088/systemNotice/single";
        // 入参
        String title = "活动奖励到账啦！";
        String content = "恭喜您，您在本次转发唐某人空间说说的活动中总共获得" + exp + "经验值，已经发放到您的账号，请注意查收。";
        Integer userId = user.getId();
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("title", title);
        map.add("content", content);
        map.add("userId", userId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MjEzOTMyNDUsInN1YiI6MywiY3JlYXRlZCI6MTYwNTg0MTI0NTQ4OX0.FDsrt38Ch-4I9J7vN2V8C2rBXgm99Pl04P28QSZfhYkVVJMzRYd7FTz17o1DwCgnKYBQkpGMvFHmGjVehEkLEQ");
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(map, headers);
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return ResponseVO.success();
    }


    /* 删除帖子接口
    @DeleteMapping("/post/{postId}")
    public ResponseVO deletePost(@PathVariable Integer postId, @PathParam("password")String password) {
        if (!"allsparkstudio".equals(password)) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION, "密码错误");
        }
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post == null) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "帖子不存在");
        }
        postMapper.deleteByPrimaryKey(postId);
        // 通过MQ同步删除ES数据
        rabbitTemplate.convertAndSend("MySQL2ESPostExchange", "delete", post);
        // 删除帖子下面的评论对应的用户-评论关系表，这样用户被点赞数不会减只会加
        List<Integer> commentIdList = commentMapper.selectIdsByPostId(postId);
        for (Integer commentId : commentIdList) {
            userCommentLikeMapper.deleteByCommentId(commentId);
            userCommentCoinMapper.deleteByCommentId(commentId);
        }
        commentMapper.deleteByPostId(postId);
        // 删除用户-帖子-点赞关联动作
        userPostLikeMapper.deleteByPostId(postId);
        // 删除用户-帖子-收藏关联动作
        userPostFavoriteMapper.deleteByPostId(postId);
        // 删除用户-帖子-投币关联动作
        userPostCoinMapper.deleteByPostId(postId);
        // 删除帖子和分类的关系
        postCategoryMapper.deleteByPostId(postId);
        // 删除帖子和圈子的关系
        postCircleMapper.deleteByPostId(postId);
        return ResponseVO.success();
    }
    */
}
