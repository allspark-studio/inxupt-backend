package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.Follow;

public interface FollowMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Follow record);

    int insertSelective(Follow record);

    Follow selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Follow record);

    int updateByPrimaryKey(Follow record);

    int countByUserId(Integer userId);

    int countByFollowedUserId(Integer userId);

    boolean isFollowed(Integer myId, Integer hisId);

    int updateFollow(Follow follow);

    Integer selectFansIdList(Integer userId);

    Integer selectFollowsIdList(Integer userId);

}