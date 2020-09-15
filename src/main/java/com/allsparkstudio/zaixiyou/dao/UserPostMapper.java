package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.UserPost;

public interface UserPostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPost record);

    int insertSelective(UserPost record);

    UserPost selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPost record);

    int updateByPrimaryKey(UserPost record);

    int countLikeByUserId(Integer userId);

    int countFavoriteByUserId(Integer userId);

    int countLikeByPostId(Integer postId);

    int countFavoriteByPostId(Integer postId);

    int countCoinsByPostId(Integer postId);

    UserPost selectByUserIdAndPostId(Integer userId, Integer postId);

    int updateState(UserPost userPost);

    void deleteByPostId(Integer postId);
}