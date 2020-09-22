package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.SMS;

import java.util.List;

public interface SMSMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SMS record);

    int insertSelective(SMS record);

    SMS selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SMS record);

    int updateByPrimaryKey(SMS record);

    List<SMS> selectAll();
}