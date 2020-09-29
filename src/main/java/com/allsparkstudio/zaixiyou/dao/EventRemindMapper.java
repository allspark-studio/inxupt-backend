package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.EventRemind;

import java.util.List;

public interface EventRemindMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EventRemind record);

    int insertSelective(EventRemind record);

    EventRemind selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EventRemind record);

    int updateByPrimaryKey(EventRemind record);

    List<EventRemind> selectReplyNoticesByUserId(Integer userId);

    int updateState(EventRemind replyNotice);

    Integer countUnreadNoticeByUserId(Integer userId);

    List<EventRemind> selectNewsNoticesByUserId(Integer userId);

    List<EventRemind> selectAtNoticesByUserId(Integer userId);
}