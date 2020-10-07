package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.dao.EventRemindMapper;
import com.allsparkstudio.zaixiyou.dao.SystemNoticeMapper;
import com.allsparkstudio.zaixiyou.dao.UserMapper;
import com.allsparkstudio.zaixiyou.dao.UserSystemNoticeMapper;
import com.allsparkstudio.zaixiyou.enums.RemindActionEnum;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.po.EventRemind;
import com.allsparkstudio.zaixiyou.pojo.po.SystemNotice;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import com.allsparkstudio.zaixiyou.pojo.po.UserSystemNotice;
import com.allsparkstudio.zaixiyou.pojo.vo.*;
import com.allsparkstudio.zaixiyou.service.NoticeService;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    EventRemindMapper eventRemindMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserSystemNoticeMapper userSystemNoticeMapper;

    @Autowired
    SystemNoticeMapper systemNoticeMapper;

    @Override
    public ResponseVO countUnreadNotice(String token) {
        int noticeNum = 0;
        if (jwtUtils.validateToken(token)) {
            Integer userId = jwtUtils.getIdFromToken(token);
            Integer remindNoticeNum = eventRemindMapper.countUnreadNoticeByUserId(userId);
            Integer systemNoticeNum = userSystemNoticeMapper.countUnreadNoticeByUserId(userId);
            noticeNum = remindNoticeNum + systemNoticeNum;
        }
        return ResponseVO.success(noticeNum);
    }

    @Override
    public ResponseVO listReplyNotices(String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        List<EventRemind> replyNotices = eventRemindMapper.selectReplyNoticesByUserId(userId);
        List<ReplyNoticeVO> replyNoticeVOList = new ArrayList<>();
        for (EventRemind replyNotice : replyNotices) {
            ReplyNoticeVO replyNoticeVO = new ReplyNoticeVO();
            replyNoticeVO.setSenderId(replyNotice.getSenderId());
            replyNoticeVO.setSourceContent(replyNotice.getSourceContent());
            replyNoticeVO.setReplyContent(replyNotice.getReplyContent());
            replyNoticeVO.setPostType(replyNotice.getPostType());
            if (replyNotice.getAction().equals(RemindActionEnum.REPLY_POST.getCode())) {
                replyNoticeVO.setSourceType(1);
            }else {
                replyNoticeVO.setSourceType(2);
            }
            replyNoticeVO.setSourceId(replyNotice.getSourceId());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            replyNoticeVO.setSendTime(simpleDateFormat.format(replyNotice.getRemindTime()));
            replyNoticeVOList.add(replyNoticeVO);
            replyNotice.setState(1);
            eventRemindMapper.updateState(replyNotice);
        }
        return ResponseVO.success(replyNoticeVOList);
    }

    @Override
    public ResponseVO listNewsNotices(String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        List<EventRemind> newsNotices = eventRemindMapper.selectNewsNoticesByUserId(userId);
        List<NewsNoticeVO> newsNoticeVOList = new ArrayList<>();
        for (EventRemind newsNotice : newsNotices) {
            NewsNoticeVO newsNoticeVO = new NewsNoticeVO();
            newsNoticeVO.setSenderId(newsNotice.getSenderId());
            newsNoticeVO.setSourceId(newsNotice.getSourceId());
            newsNoticeVO.setSourceContent(newsNotice.getSourceContent());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            newsNoticeVO.setSendTime(simpleDateFormat.format(newsNotice.getRemindTime()));
            newsNoticeVO.setPostType(newsNotice.getPostType());
            switch (newsNotice.getAction()) {
                case 1:
                    newsNoticeVO.setAction("点赞");
                    newsNoticeVO.setSourceType("帖子");
                    break;
                case 2:
                    newsNoticeVO.setAction("点赞");
                    newsNoticeVO.setSourceType("评论");
                    break;
                case 3:
                    newsNoticeVO.setAction("投币");
                    newsNoticeVO.setSourceType("帖子");
                    break;
                case 4:
                    newsNoticeVO.setAction("投币");
                    newsNoticeVO.setSourceType("评论");
                    break;
                case 5:
                    newsNoticeVO.setAction("收藏");
                    newsNoticeVO.setSourceType("帖子");
                    break;
                case 8:
                    newsNoticeVO.setAction("关注");
                default:
                    break;
            }
            newsNoticeVOList.add(newsNoticeVO);
            newsNotice.setState(1);
            eventRemindMapper.updateState(newsNotice);
        }
        return ResponseVO.success(newsNoticeVOList);
    }

    @Override
    public ResponseVO listAtNotices(String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        List<EventRemind> atNotices = eventRemindMapper.selectAtNoticesByUserId(userId);
        List<AtNoticeVO> atNoticeVOList = new ArrayList<>();
        for (EventRemind atNotice : atNotices) {
            AtNoticeVO atNoticeVO = new AtNoticeVO();
            atNoticeVO.setSenderId(atNotice.getSenderId());
            atNoticeVO.setSourceContent(atNotice.getSourceContent());
            atNoticeVO.setSourceId(atNotice.getSourceId());
            atNoticeVO.setPostType(atNotice.getPostType());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            atNoticeVO.setSendTime(simpleDateFormat.format(atNotice.getRemindTime()));
            atNoticeVOList.add(atNoticeVO);
            atNotice.setState(1);
            eventRemindMapper.updateState(atNotice);
        }
        return ResponseVO.success(atNoticeVOList);
    }

    @Override
    public ResponseVO listSystemNotices(String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        List<UserSystemNotice> userSystemNotices = userSystemNoticeMapper.selectUnreadNoticesByUserId(userId);
        List<SystemNoticeVO> systemNoticeVOList = new ArrayList<>();
        for (UserSystemNotice userSystemNotice : userSystemNotices) {
            SystemNotice systemNotice = systemNoticeMapper.selectByPrimaryKey(userSystemNotice.getNoticeId());
            SystemNoticeVO systemNoticeVO = new SystemNoticeVO();
            systemNoticeVO.setTitle(systemNotice.getTitle());
            systemNoticeVO.setContent(systemNotice.getContent());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            systemNoticeVO.setPublishTime(simpleDateFormat.format(systemNotice.getPublishTime()));
            systemNoticeVOList.add(systemNoticeVO);
            userSystemNotice.setState(1);
            userSystemNoticeMapper.updateState(userSystemNotice);
        }
        return ResponseVO.success(systemNoticeVOList);
    }
}