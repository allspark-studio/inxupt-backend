package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.UserCommentLike;

public interface UserCommentLikeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCommentLike record);

    int insertSelective(UserCommentLike record);

    UserCommentLike selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCommentLike record);

    int updateByPrimaryKey(UserCommentLike record);

    UserCommentLike selectByUserIdAndCommentId(Integer userId, Integer commentId);

    int countByCommentId(Integer commentId);

    void deleteByCommentId(Integer commentId);
}