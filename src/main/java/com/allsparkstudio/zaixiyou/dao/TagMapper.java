package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface TagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tag record);

    int insertSelective(Tag record);

    Tag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);

    List<Tag> selectByIdSet(@Param("idSet") Set<Integer> idSet);

    Tag selectByName(String name);
}