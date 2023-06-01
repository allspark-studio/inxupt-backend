package com.allsparkstudio.zaixiyou.controller;

import com.allsparkstudio.zaixiyou.pojo.form.AddArticleForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddPostForm;
import com.allsparkstudio.zaixiyou.pojo.po.Category;
import com.allsparkstudio.zaixiyou.pojo.vo.PostVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.PostService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 学术文章相关的controller
 *
 * @author AlkaidChen
 * @date 2020/8/19
 */
@RestController
@Slf4j
@Api(tags = "帖子/文章")
public class PostController {

    @Autowired
    private PostService postService;


    @GetMapping("/posts")
    @ApiOperation("展示全部帖子")
    public ResponseVO<PageInfo> listAll(Integer categoryId,
                                        @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false, defaultValue = "1") Integer sortedBy,
                                        @RequestHeader(value = "token", required = false) String token) {
        return postService.listAll(categoryId, null, null, null, token, pageNum, pageSize, sortedBy);
    }

    @PostMapping("/article")
    @ApiOperation("发布文章")
    public ResponseVO<Map<String, Integer>> addArticle(@RequestBody AddArticleForm addArticleForm,
                                                       @RequestHeader(value = "token", required = false) String token) {
        return postService.addArticle(addArticleForm, token);

    }

    @PostMapping("/post")
    @ApiOperation("发布帖子")
    public ResponseVO<Map<String, Integer>> addPost(@RequestBody AddPostForm addPostForm,
                                                    @RequestHeader(value = "token", required = false) String token) {
        return postService.addPost(addPostForm, token);
    }

    @DeleteMapping("/post/{postId}")
    @ApiOperation("删除帖子或文章")
    public ResponseVO deletePost(@PathVariable("postId") Integer postId,
                                 @RequestHeader(value = "token", required = false) String token) {
        return postService.deletePost(postId, token);
    }

    @PostMapping("/post/{postId}/like")
    @ApiOperation("点赞帖子")
    public ResponseVO like(@PathVariable("postId") Integer postId,
                           @RequestHeader(value = "token", required = false) String token) {
        return postService.like(postId, token);
    }

    @DeleteMapping("/post/{postId}/like")
    @ApiOperation("取消帖子点赞")
    public ResponseVO dislike(@PathVariable("postId") Integer postId,
                              @RequestHeader(value = "token", required = false) String token) {
        return postService.dislike(postId, token);
    }

    @PostMapping("/post/{postId}/favorite")
    @ApiOperation("收藏帖子")
    public ResponseVO favorite(@PathVariable("postId") Integer postId,
                               @RequestHeader(value = "token", required = false) String token) {
        return postService.favorite(postId, token);
    }

    @DeleteMapping("/post/{postId}/favorite")
    @ApiOperation("取消收藏帖子")
    public ResponseVO disFavorite(@PathVariable("postId") Integer postId,
                                  HttpServletRequest request) {
        String token = request.getHeader("token");
        return postService.disFavorite(postId, token);
    }

    @PostMapping("/post/{postId}/coin")
    @ApiOperation("投币帖子")
    public ResponseVO coin(@PathVariable("postId") Integer postId,
                           @RequestHeader(value = "token", required = false) String token) {
        return postService.coin(postId, token);
    }

    @GetMapping("/post/{postId}")
    @ApiOperation("帖子/文章详情页")
    public ResponseVO<PostVO> getPost(@PathVariable("postId") Integer postId,
                                      @RequestHeader(value = "token", required = false) String token) {
        return postService.getPost(postId, token);
    }

    @GetMapping("/categories")
    @ApiOperation("展示帖子分类列表")
    public ResponseVO<List<Category>> listCategories() {
        return postService.listCategories();
    }
}
