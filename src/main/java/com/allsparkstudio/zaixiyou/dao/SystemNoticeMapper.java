package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.SystemNotice;

public interface SystemNoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemNotice record);

    int insertSelective(SystemNotice record);

    SystemNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemNotice record);

    int updateByPrimaryKey(SystemNotice record);
}