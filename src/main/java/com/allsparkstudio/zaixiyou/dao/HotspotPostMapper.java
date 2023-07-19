package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.HotspotPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HotspotPostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HotspotPost record);

    int insertSelective(HotspotPost record);

    HotspotPost selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HotspotPost record);

    int updateByPrimaryKey(HotspotPost record);

    List<HotspotPost> selectAll();

    List<HotspotPost> selectByCollectionId(Integer collectionId);
}