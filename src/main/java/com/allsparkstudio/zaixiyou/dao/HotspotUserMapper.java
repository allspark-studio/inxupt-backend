package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.HotspotUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HotspotUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HotspotUser record);

    int insertSelective(HotspotUser record);

    HotspotUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HotspotUser record);

    int updateByPrimaryKey(HotspotUser record);

    List<HotspotUser> selectAll();
}