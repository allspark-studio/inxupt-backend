package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.RestaurantSwiper;

import java.util.List;

public interface RestaurantSwiperMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RestaurantSwiper record);

    int insertSelective(RestaurantSwiper record);

    RestaurantSwiper selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RestaurantSwiper record);

    int updateByPrimaryKey(RestaurantSwiper record);

    List<RestaurantSwiper> selectByRestaurantId(Integer restaurantId);
}