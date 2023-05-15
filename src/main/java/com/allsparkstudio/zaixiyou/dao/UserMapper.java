package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    User selectByOpenID(String openID);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByPhone(String phone);

    List<User> selectAll();

    List<User> selectFans(Integer userId);

    List<User> selectFollowers(Integer userId);


    int updateLastActiveTime(User user);

    int updateInsertableCoins(User record);

    int updateExchangeableCoins(User record);

    int updateLikeNum(User record);

    int updateExpAndLv(User record);

    int updateAvatar(User user);

    int updateBackground(User user);
}