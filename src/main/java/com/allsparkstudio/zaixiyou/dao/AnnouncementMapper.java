package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.Announcement;

import java.util.List;

public interface AnnouncementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Announcement record);

    int insertSelective(Announcement record);

    Announcement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Announcement record);

    int updateByPrimaryKey(Announcement record);

    List<Announcement> selectAll(Integer circleId);

    Announcement selectFirstAnnouncement(Integer circleId);

    Announcement selectSecondAnnouncement(Integer circleId);

    int updateState(Announcement record);
}