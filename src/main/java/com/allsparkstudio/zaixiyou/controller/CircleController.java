package com.allsparkstudio.zaixiyou.controller;

import com.allsparkstudio.zaixiyou.pojo.form.AddCircleArticleForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddCircleForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddAnnouncementForm;
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
                                          @RequestParam(required = false, defaultValue = "1") Integer sortedBy,
                                          @RequestHeader(value = "token", required = false) String token) {
        return postService.listAll(null, null, circleId, null, null, token, pageNum, pageSize, sortedBy);
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

    @PostMapping("/circle/{circleId}/announcement")
    @ApiOperation("发布公告")
    public ResponseVO addAnnouncement(@PathVariable("circleId") Integer circleId,
                                      @RequestBody AddAnnouncementForm form,
                                      @RequestHeader(value = "token", required = false) String token) {
        return circleService.addAnnouncement(circleId, form, token);
    }

    @PostMapping("/announcement/{announcementId}/top")
    @ApiOperation("置顶公告")
    public ResponseVO setTopAnnouncement(@PathVariable("announcementId") Integer announcementId,
                                         @RequestHeader(value = "token", required = false) String token) {
        return circleService.toggleTopAnnouncement(announcementId, true, token);
    }

    @DeleteMapping("/announcement/{announcementId}/top")
    @ApiOperation("取消置顶公告")
    public ResponseVO unsetTopAnnouncement(@PathVariable("announcementId") Integer announcementId,
                                   @RequestHeader(value = "token", required = false) String token) {
        return circleService.toggleTopAnnouncement(announcementId, false, token);
    }

    @DeleteMapping("/announcement/{announcementId}")
    @ApiOperation("删除公告")
    public ResponseVO deleteAnnouncement(@PathVariable("announcementId") Integer announcementId,
                                   @RequestHeader(value = "token", required = false) String token) {
        return circleService.deleteAnnouncement(announcementId, token);
    }


    @GetMapping("/circle/{circleId}/announcements/")
    @ApiOperation("公告列表")
    public ResponseVO<List<AnnouncementInListVO>> listAnnouncements(@PathVariable("circleId") Integer circleId,
                                                              @RequestHeader(value = "token", required = false) String token) {
        return circleService.listAnnouncements(circleId, token);
    }

    @GetMapping("/circle/announcement/{announcementId}")
    @ApiOperation("公告详情")
    public ResponseVO<AnnouncementVO> getAnnouncement(@PathVariable("announcementId") Integer announcementId) {
        return circleService.getAnnouncement(announcementId);
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
