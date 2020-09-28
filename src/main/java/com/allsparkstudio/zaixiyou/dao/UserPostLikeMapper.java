package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.UserPostLike;

public interface UserPostLikeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPostLike record);

    int insertSelective(UserPostLike record);

    UserPostLike selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPostLike record);

    int updateByPrimaryKey(UserPostLike record);

    UserPostLike selectByUserIdAndPostId(Integer userId, Integer postId);

    int countByPostId(Integer postId);

    void deleteByPostId(Integer postId);
}