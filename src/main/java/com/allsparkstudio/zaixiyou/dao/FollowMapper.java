package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.Follow;

import java.util.List;

public interface FollowMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Follow record);

    int insertSelective(Follow record);

    Follow selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Follow record);

    int updateByPrimaryKey(Follow record);

    Integer countByUserId(Integer userId);

    Integer countByFollowedUserId(Integer userId);

    boolean isFollowed(Integer myId, Integer hisId);

    int updateFollow(Follow record);

    List<Integer> selectFansIdList(Integer userId);

    List<Integer> selectFollowsIdList(Integer userId);
}