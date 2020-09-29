package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.dao.EventRemindMapper;
import com.allsparkstudio.zaixiyou.dao.UserMapper;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.po.EventRemind;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import com.allsparkstudio.zaixiyou.pojo.vo.AtNoticeVO;
import com.allsparkstudio.zaixiyou.pojo.vo.NewsNoticeVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ReplyNoticeVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
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

    @Override
    public ResponseVO countUnreadNotice(String token) {
        Integer noticeNum = 0;
        if (jwtUtils.validateToken(token)) {
            Integer userId = jwtUtils.getIdFromToken(token);
            noticeNum = eventRemindMapper.countUnreadNoticeByUserId(userId);
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
            User sender = userMapper.selectByPrimaryKey(userId);
            replyNoticeVO.setSenderName(sender.getNickname());
            replyNoticeVO.setSenderAvatar(sender.getAvatarUrl());
            replyNoticeVO.setSourceContent(replyNotice.getSourceContent());
            replyNoticeVO.setReplyContent(replyNotice.getReplyContent());
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
            User sender = userMapper.selectByPrimaryKey(newsNotice.getSenderId());
            newsNoticeVO.setSenderAvatar(sender.getAvatarUrl());
            newsNoticeVO.setSenderName(sender.getNickname());
            newsNoticeVO.setSourceId(newsNotice.getSourceId());
            newsNoticeVO.setSourceContent(newsNotice.getSourceContent());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
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
                    newsNoticeVO.setAction("收藏");
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
            User sender = userMapper.selectByPrimaryKey(userId);
            atNoticeVO.setSenderName(sender.getNickname());
            atNoticeVO.setSenderAvatar(sender.getAvatarUrl());
            atNoticeVO.setSourceContent(atNotice.getSourceContent());
            atNoticeVO.setSourceId(atNotice.getSourceId());
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
        return null;
    }
}
