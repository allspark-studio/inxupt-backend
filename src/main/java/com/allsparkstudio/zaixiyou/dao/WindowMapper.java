package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.Window;

import java.util.List;

public interface WindowMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Window record);

    int insertSelective(Window record);

    Window selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Window record);

    int updateByPrimaryKey(Window record);

    List<Window> selectByRestaurantId(Integer restaurantId);
}