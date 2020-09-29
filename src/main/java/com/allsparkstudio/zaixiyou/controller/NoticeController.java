package com.allsparkstudio.zaixiyou.controller;

import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "事件提醒")
public class NoticeController {

    @Autowired
    NoticeService noticeService;


    @GetMapping("/notice/num")
    @ApiOperation("未读消息数量")
    public ResponseVO countUnreadNotice(@RequestHeader(value = "token", required = false) String token) {
        return noticeService.countUnreadNotice(token);
    }

    @GetMapping("/notices/reply")
    @ApiOperation("评论/回复")
    public ResponseVO listReplyNotices(@RequestHeader(value = "token", required = false) String token) {
        return noticeService.listReplyNotices(token);
    }

    @GetMapping("/notices/news")
    @ApiOperation("动态消息")
    public ResponseVO listNewsNotices(@RequestHeader(value = "token", required = false) String token) {
        return noticeService.listNewsNotices(token);
    }

    @GetMapping("/notices/at")
    @ApiOperation("提到了我")
    public ResponseVO listAtNotices(@RequestHeader(value = "token", required = false) String token) {
        return noticeService.listAtNotices(token);
    }

    @GetMapping("/notices/system")
    @ApiOperation("系统通知")
    public ResponseVO listSystemNotices(@RequestHeader(value = "token", required = false) String token) {
        return noticeService.listSystemNotices(token);
    }
}
