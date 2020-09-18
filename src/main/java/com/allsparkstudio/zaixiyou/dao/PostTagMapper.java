package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.PostTag;

import java.util.List;
import java.util.Set;

public interface PostTagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostTag record);

    int insertSelective(PostTag record);

    PostTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostTag record);

    int updateByPrimaryKey(PostTag record);

    Set<Integer> selectTagIdsByPostId(Integer postId);

    int deleteByPostId(Integer postId);
}