package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.PostCircle;

public interface PostCircleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostCircle record);

    int insertSelective(PostCircle record);

    PostCircle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostCircle record);

    int updateByPrimaryKey(PostCircle record);

    Integer countPostsByCircleId(Integer circleId);

    int deleteByPostId(Integer postId);
}