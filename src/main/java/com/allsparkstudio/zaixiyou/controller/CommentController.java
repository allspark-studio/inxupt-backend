package com.allsparkstudio.zaixiyou.controller;

import com.allsparkstudio.zaixiyou.pojo.form.AddCommentForm;
import com.allsparkstudio.zaixiyou.pojo.vo.CommentVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author AlkaidChen
 * @date 2020/8/19
 */
@Slf4j
@RestController
@Api(tags = "评论")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    @ApiOperation("帖子发表评论")
    public ResponseVO<CommentVO> addComment(@PathVariable("postId") Integer contentId,
                                                @RequestBody AddCommentForm addCommentForm,
                                                @RequestHeader(value = "token", required = false) String token) {
        return commentService.addComment(contentId, token, addCommentForm);
    }

    @DeleteMapping("/comment/{commentId}")
    @ApiOperation("删除评论")
    public ResponseVO deleteComment(@PathVariable("commentId") Integer commentId,
                                    @RequestHeader(value = "token", required = false) String token) {
        return commentService.deleteComment(commentId, token);
    }

    @PostMapping("/comment/{commentId}/like")
    @ApiOperation("点赞评论")
    public ResponseVO like(@PathVariable("commentId") Integer commentId,
                           @RequestHeader(value = "token", required = false) String token){
        return commentService.like(commentId, token);
    }

    @DeleteMapping("/comment/{commentId}/like")
    @ApiOperation("取消点赞评论")
    public ResponseVO dislike(@PathVariable("commentId") Integer commentId,
                              @RequestHeader(value = "token", required = false) String token){
        return commentService.dislike(commentId, token);
    }

    @PostMapping("/comment/{commentId}/coin")
    @ApiOperation("投币评论")
    public ResponseVO coin(@PathVariable("commentId") Integer commentId,
                           @RequestHeader(value = "token", required = false) String token) {
        return commentService.coin(commentId, token);
    }

    @GetMapping("/post/{postId}/comments")
    @ApiOperation("获取帖子下面的全部评论")
    public ResponseVO listComments(@PathVariable("postId") Integer postId,
                                   @RequestParam(required = false, defaultValue = "2") Integer sortedBy,
                                   @RequestHeader(value = "token", required = false) String token) {
        return commentService.listAll(postId, token, sortedBy);
    }
}
