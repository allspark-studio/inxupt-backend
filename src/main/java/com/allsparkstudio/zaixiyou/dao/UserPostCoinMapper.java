package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.UserPostCoin;

public interface UserPostCoinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPostCoin record);

    int insertSelective(UserPostCoin record);

    UserPostCoin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPostCoin record);

    int updateByPrimaryKey(UserPostCoin record);

    UserPostCoin selectByUserIdAndPostId(Integer userId, Integer postId);
}