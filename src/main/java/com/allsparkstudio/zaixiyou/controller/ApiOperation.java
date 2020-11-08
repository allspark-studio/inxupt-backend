package com.allsparkstudio.zaixiyou.controller;


import com.allsparkstudio.zaixiyou.dao.CircleMapper;
import com.allsparkstudio.zaixiyou.dao.PostMapper;
import com.allsparkstudio.zaixiyou.dao.UserMapper;
import com.allsparkstudio.zaixiyou.enums.PostTypeEnum;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.ESEntity.ESCircle;
import com.allsparkstudio.zaixiyou.pojo.ESEntity.ESPost;
import com.allsparkstudio.zaixiyou.pojo.ESEntity.ESUser;
import com.allsparkstudio.zaixiyou.pojo.po.Circle;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 第三方调用的Api接口
 * 这个只是暂时这么用，所以没有分三层架构
 * 如果要更进一步完善功能，一定要把业务下放到service层
 * @author 陈帅
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
}
