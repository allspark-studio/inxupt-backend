package com.allsparkstudio.zaixiyou.controller;

import com.allsparkstudio.zaixiyou.pojo.form.AddCircleArticleForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddCircleForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddCircleNoticeForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddCirclePostForm;
import com.allsparkstudio.zaixiyou.pojo.vo.*;
import com.allsparkstudio.zaixiyou.service.CircleService;
import com.allsparkstudio.zaixiyou.service.PostService;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 陈帅
 * @date 2020/8/24
 */
@RestController
@Api(tags = "圈子")
public class CircleController {

    @Autowired
    CircleService circleService;

    @Autowired
    PostService postService;

    @Autowired
    JWTUtils jwtUtils;

    @GetMapping("/circle/{circleId}/posts")
    @ApiOperation("列出圈子里的帖子列表")
    public ResponseVO<PageInfo> listPosts(@PathVariable("circleId") Integer circleId,
                                          @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                          @RequestHeader(value = "token", required = false) String token) {
        return postService.listAll(null, null, circleId, null, null, token, pageNum, pageSize);
    }

    @PostMapping("/circle")
    @ApiOperation("新建圈子")
    public ResponseVO<Map<String, Integer>> addCircle(@RequestBody AddCircleForm addCircleForm,
                                                      @RequestHeader(value = "token", required = false) String token) {
        return circleService.addCircle(addCircleForm, token);
    }

    @GetMapping("/circles")
    @ApiOperation("全部圈子")
    public ResponseVO<PageInfo> listAll(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return circleService.listAll(pageNum, pageSize);
    }

    @GetMapping("/user/{userId}/circles")
    @ApiOperation("用户的圈子列表")
    public ResponseVO<List<CircleInListVO>> listCircles(@PathVariable("userId") Integer userId,
                                                        @RequestHeader(value = "token", required = false) String token) {
        return circleService.listCircles(userId, token);
    }

    @PostMapping("/circle/{circleId}/notice")
    @ApiOperation("发布公告")
    public ResponseVO addNotice(@PathVariable("circleId") Integer circleId,
                                @RequestBody AddCircleNoticeForm form,
                                @RequestHeader(value = "token", required = false) String token) {
        return circleService.addNotice(circleId, form, token);
    }

    @PostMapping("/notice/{noticeId}/top")
    @ApiOperation("置顶公告")
    public ResponseVO setTopNotice(@PathVariable("noticeId") Integer noticeId,
                                   @RequestHeader(value = "token", required = false) String token) {
        return circleService.toggleTopNotice(noticeId, true, token);
    }

    @DeleteMapping("/notice/{noticeId}/top")
    @ApiOperation("取消置顶公告")
    public ResponseVO unsetTopNotice(@PathVariable("noticeId") Integer noticeId,
                                   @RequestHeader(value = "token", required = false) String token) {
        return circleService.toggleTopNotice(noticeId, false, token);
    }

    @DeleteMapping("/notice/{noticeId}")
    @ApiOperation("删除公告")
    public ResponseVO deleteNotice(@PathVariable("noticeId") Integer noticeId,
                                   @RequestHeader(value = "token", required = false) String token) {
        return circleService.deleteNotice(noticeId, token);
    }


    @GetMapping("/circle/{circleId}/notices/")
    @ApiOperation("公告列表")
    public ResponseVO<List<NoticeInListVO>> listNotices(@PathVariable("circleId") Integer circleId,
                                                        @RequestHeader(value = "token", required = false) String token) {
        return circleService.listNotices(circleId, token);
    }

    @GetMapping("/circle/notice/{noticeId}")
    @ApiOperation("公告详情")
    public ResponseVO<NoticeVO> getNotice(@PathVariable("noticeId") Integer noticeId) {
        return circleService.getNotice(noticeId);
    }

    @GetMapping("/circle/{circleId}")
    @ApiOperation("圈子详情")
    public ResponseVO<CircleDetailVO> getCircle(@PathVariable("circleId") Integer circleId,
                                                @RequestHeader(value = "token", required = false) String token) {
        return circleService.getCircle(circleId, token);
    }

    @PostMapping("/circle/{circleId}/post")
    @ApiOperation("圈子内发帖子")
    public ResponseVO<Map<String, Integer>> addPost(@PathVariable("circleId") Integer circleId,
                                                    @RequestBody AddCirclePostForm form,
                                                    @RequestHeader(value = "token", required = false) String token) {
        return postService.addPostInCircle(circleId, form, token);
    }

    @PostMapping("/circle/{circleId}/article")
    @ApiOperation("圈子内发文章")
    public ResponseVO<Map<String, Integer>> addArticle(@PathVariable("circleId") Integer circleId,
                                                       @RequestBody AddCircleArticleForm form,
                                                       @RequestHeader(value = "token", required = false) String token) {
        return postService.addArticleInCircle(circleId, form, token);
    }

    @PostMapping("/circle/{circleId}/follow")
    @ApiOperation("关注圈子")
    public ResponseVO follow(@PathVariable("circleId") Integer circleId,
                             @RequestHeader(value = "token", required = false) String token) {
        return circleService.toggleFollow(circleId, token, true);
    }

    @DeleteMapping("/circle/{circleId}/follow")
    @ApiOperation("取消关注圈子")
    public ResponseVO disFollow(@PathVariable("circleId") Integer circleId,
                             @RequestHeader(value = "token", required = false) String token) {
        return circleService.toggleFollow(circleId, token, false);
    }

    @GetMapping("/circle/{circleId}/members")
    @ApiOperation("圈子成员列表")
    public ResponseVO<List<CircleMemberVO>> listMember(@PathVariable("circleId") Integer circleId,
                                                       @RequestHeader(value = "token", required = false) String token) {
        return circleService.listMembers(circleId, token);
    }
}
