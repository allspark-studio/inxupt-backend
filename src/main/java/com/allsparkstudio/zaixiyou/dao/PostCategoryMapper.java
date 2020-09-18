package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.PostCategory;

public interface PostCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostCategory record);

    int insertSelective(PostCategory record);

    PostCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostCategory record);

    int updateByPrimaryKey(PostCategory record);

    int deleteByPostId(Integer postId);
}