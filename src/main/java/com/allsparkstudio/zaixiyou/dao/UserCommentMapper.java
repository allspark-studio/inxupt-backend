package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.UserComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserComment record);

    int insertSelective(UserComment record);

    UserComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserComment record);

    int updateByPrimaryKey(UserComment record);

    UserComment selectByUserIdAndCommentId(Integer userId, Integer commentId);

    List<UserComment> selectByCommentId(Integer commentId);

    Integer countLikeByCommentId(Integer commentId);

    Integer countCoinsByCommentId(Integer commentId);

    int updateState(UserComment record);

    void deleteByCommentId(Integer commentId);
}