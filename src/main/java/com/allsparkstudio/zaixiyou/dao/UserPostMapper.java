package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.UserPost;

public interface UserPostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPost record);

    int insertSelective(UserPost record);

    UserPost selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPost record);

    int updateByPrimaryKey(UserPost record);

    Integer countFavoriteByUserId(Integer userId);

    UserPost selectByUserIdAndPostId(Integer userId, Integer postId);

    Integer countLikeByPostId(Integer postId);

    Integer countFavoriteByPostId(Integer postId);

    Integer countCoinsByPostId(Integer postId);

    int updateState(UserPost record);

    int deleteByPostId(Integer postId);
}