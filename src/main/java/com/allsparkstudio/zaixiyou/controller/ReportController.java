package com.allsparkstudio.zaixiyou.controller;


import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.enums.ReportTypeEnum;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.form.ReportForm;
import com.allsparkstudio.zaixiyou.pojo.po.*;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "举报")
@Slf4j
public class ReportController {

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    PostMapper postMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    CircleMapper circleMapper;

    @Autowired
    UserCircleMapper userCircleMapper;

    @Autowired
    ReportMapper reportMapper;

    @PostMapping("/post/{postId}/report")
    @ApiOperation("举报帖子")
    public ResponseVO reportPost(@PathVariable("postId") Integer postId,
                                 @RequestBody ReportForm form,
                                 @RequestHeader(value = "token", required = false) String token) {
        Report report = new Report();
        report.setType(ReportTypeEnum.POST.getCode());
        report.setItemId(postId);
        if (jwtUtils.validateToken(token)) {
            report.setUserId(jwtUtils.getIdFromToken(token));
        }
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post == null) {
            log.error("请求的帖子不存在，postId:[{}]", postId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "帖子不存在");
        }
        report.setItemAuthorId(post.getAuthorId());
        if (!StringUtils.isEmpty(form.getDetail())) {
            report.setDetail(form.getDetail());
        }
        if (form.getReasons() != null && form.getReasons().size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String reason : form.getReasons()) {
                stringBuilder.append(reason);
                stringBuilder.append(";");
            }
            report.setReasons(stringBuilder.substring(0, stringBuilder.length() - 1));
        }
        int result = reportMapper.insertSelective(report);
        if (result != 1) {
            log.error("举报帖子/文章时出现错误，数据库表’report‘更新失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        return ResponseVO.success();
    }

    @PostMapping("/comment/{commentId}/report")
    @ApiOperation("举报评论")
    public ResponseVO reportComment(@PathVariable("commentId") Integer commentId,
                                 @RequestBody ReportForm form,
                                 @RequestHeader(value = "token", required = false) String token) {
        Report report = new Report();
        report.setType(ReportTypeEnum.COMMENT.getCode());
        report.setItemId(commentId);
        if (jwtUtils.validateToken(token)) {
            report.setUserId(jwtUtils.getIdFromToken(token));
        }
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        if (comment == null) {
            log.error("请求的评论不存在，circleId:[{}]", commentId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "评论不存在");
        }
        report.setItemAuthorId(comment.getAuthorId());
        if (!StringUtils.isEmpty(form.getDetail())) {
            report.setDetail(form.getDetail());
        }
        if (form.getReasons() != null && form.getReasons().size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String reason : form.getReasons()) {
                stringBuilder.append(reason);
                stringBuilder.append(";");
            }
            report.setReasons(stringBuilder.substring(0, stringBuilder.length() - 1));
        }
        int result = reportMapper.insertSelective(report);
        if (result != 1) {
            log.error("举报评论时出现错误，数据库表’report‘更新失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        return ResponseVO.success();
    }

    @PostMapping("/user/{userId}/report")
    @ApiOperation("举报用户")
    public ResponseVO reportUser(@PathVariable("userId") Integer userId,
                                    @RequestBody ReportForm form,
                                    @RequestHeader(value = "token", required = false) String token) {
        Report report = new Report();
        report.setType(ReportTypeEnum.USER.getCode());
        report.setItemId(userId);
        if (jwtUtils.validateToken(token)) {
            report.setUserId(jwtUtils.getIdFromToken(token));
        }
        report.setItemAuthorId(userId);
        if (!StringUtils.isEmpty(form.getDetail())) {
            report.setDetail(form.getDetail());
        }
        if (form.getReasons() != null && form.getReasons().size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String reason : form.getReasons()) {
                stringBuilder.append(reason);
                stringBuilder.append(";");
            }
            report.setReasons(stringBuilder.substring(0, stringBuilder.length() - 1));
        }
        int result = reportMapper.insertSelective(report);
        if (result != 1) {
            log.error("举报用户时出现错误，数据库表’report‘更新失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        return ResponseVO.success();
    }

    @PostMapping("/circle/{circleId}/report")
    @ApiOperation("举报圈子")
    public ResponseVO reportCircle(@PathVariable("circleId") Integer circleId,
                                    @RequestBody ReportForm form,
                                    @RequestHeader(value = "token", required = false) String token) {
        Report report = new Report();
        report.setType(ReportTypeEnum.CIRCLE.getCode());
        report.setItemId(circleId);
        if (jwtUtils.validateToken(token)) {
            report.setUserId(jwtUtils.getIdFromToken(token));
        }
        Circle circle = circleMapper.selectByPrimaryKey(circleId);
        if (circle == null) {
            log.error("请求的圈子不存在，circleId:[{}]", circle);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "圈子不存在");
        }
        Integer ownerId = userCircleMapper.selectOwnerId(circleId);
        report.setItemAuthorId(ownerId);
        if (!StringUtils.isEmpty(form.getDetail())) {
            report.setDetail(form.getDetail());
        }
        if (form.getReasons() != null && form.getReasons().size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String reason : form.getReasons()) {
                stringBuilder.append(reason);
                stringBuilder.append(";");
            }
            report.setReasons(stringBuilder.substring(0, stringBuilder.length() - 1));
        }
        int result = reportMapper.insertSelective(report);
        if (result != 1) {
            log.error("举报圈子时出现错误，数据库表’report‘更新失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        return ResponseVO.success();
    }
}
