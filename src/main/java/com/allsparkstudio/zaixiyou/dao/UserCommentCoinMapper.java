package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.UserCommentCoin;

public interface UserCommentCoinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCommentCoin record);

    int insertSelective(UserCommentCoin record);

    UserCommentCoin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCommentCoin record);

    int updateByPrimaryKey(UserCommentCoin record);

    UserCommentCoin selectByUserIdAndCommentId(Integer userId, Integer commentId);
}