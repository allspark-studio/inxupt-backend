package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.UserFoodLike;

public interface UserFoodLikeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserFoodLike record);

    int insertSelective(UserFoodLike record);

    UserFoodLike selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserFoodLike record);

    int updateByPrimaryKey(UserFoodLike record);

    UserFoodLike selectByUserIdAndFoodId(Integer userId, Integer foodId);

    int updateState(UserFoodLike userFoodLike);
}