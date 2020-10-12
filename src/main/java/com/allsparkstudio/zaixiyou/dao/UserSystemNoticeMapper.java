package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.UserSystemNotice;

import java.util.List;

public interface UserSystemNoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserSystemNotice record);

    int insertSelective(UserSystemNotice record);

    UserSystemNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserSystemNotice record);

    int updateByPrimaryKey(UserSystemNotice record);

    List<UserSystemNotice> selectUnreadNoticesByUserId(Integer userId);

    void updateState(UserSystemNotice userSystemNotice);

    Integer countUnreadNoticeByUserId(Integer userId);
}