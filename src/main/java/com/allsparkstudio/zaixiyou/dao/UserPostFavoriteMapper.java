package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.UserPostFavorite;

public interface UserPostFavoriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPostFavorite record);

    int insertSelective(UserPostFavorite record);

    UserPostFavorite selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPostFavorite record);

    int updateByPrimaryKey(UserPostFavorite record);

    UserPostFavorite selectByUserIdAndPostId(Integer userId, Integer postId);
}