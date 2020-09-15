package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.UserComment;

public interface UserCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserComment record);

    int insertSelective(UserComment record);

    UserComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserComment record);

    int updateByPrimaryKey(UserComment record);

    int countLikeByUserId(Integer userId);

    int countLikeByCommentId(Integer commentId);

    int countCoinsByCommentId(Integer commentId);

    UserComment selectByUserIdAndCommentId(Integer userId, Integer commentId);

    int updateState(UserComment userComment);
}