package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.EventRemind;

public interface EventRemindMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EventRemind record);

    int insertSelective(EventRemind record);

    EventRemind selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EventRemind record);

    int updateByPrimaryKey(EventRemind record);
}