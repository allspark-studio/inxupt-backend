package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.UserCircle;

import java.util.List;

public interface UserCircleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCircle record);

    int insertSelective(UserCircle record);

    UserCircle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCircle record);

    int updateByPrimaryKey(UserCircle record);

    List<Integer> selectCircleIdsByUserId(Integer userId);

    List<Integer> selectMemberIdsByCircleId(Integer circleId);

    Integer selectRoleOrNull(Integer userId, Integer circleId);

    int countMembers(Integer circleId);

    int updateRole(Integer userId, Integer circleId, Integer role);

    List<UserCircle> select5Members(Integer circleId);
}