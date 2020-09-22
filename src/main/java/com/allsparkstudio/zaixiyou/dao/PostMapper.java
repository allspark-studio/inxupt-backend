package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.Post;

import java.util.List;

public interface PostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Post record);

    int insertSelective(Post record);

    Post selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKey(Post record);

    Integer countByAuthorIdAndType(Integer authorId, Integer type);

    List<Post> selectAllByTime();

    List<Post> selectAllByHeat();

    List<Post> selectAllByAuthorId(Integer authorId);

    List<Post> selectByUserIdAndType(Integer authorId, Integer type);

    List<Post> selectPostsByCircleIdSortedByTime(Integer circleId);

    List<Post> selectPostsByCircleIdSortedByHeat(Integer circleId);

    List<Post> selectPostsByCategoryIdSortedByTime(Integer categoryId);

    List<Post> selectPostsByCategoryIdSortedByHeat(Integer categoryId);

    List<Post> selectFavoritesPostsByUserIdAndType(Integer userId, Integer type);

    void updateHeat(Post post);
}