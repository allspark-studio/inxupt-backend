package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.Comment;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> selectByPostIdSortedByTime(Integer postId);

    List<Comment> selectByPostIdSortedByHeat(Integer postId);

    Integer countCommentsByPostId(Integer postId);

    int deleteByPostId(Integer postId);

    List<Comment> selectSubComments(Integer rootId);

    List<Integer> selectIdsByPostId(Integer postId);

    List<Comment> selectAllByAuthorId(Integer userId);

    List<Comment> selectAll();

    Integer countSubCommentsByCommentId(Integer commentId);

    void updateHeat(Comment comment);
}