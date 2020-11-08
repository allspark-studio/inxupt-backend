package com.allsparkstudio.zaixiyou.service;

import com.allsparkstudio.zaixiyou.pojo.vo.FoodVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.pojo.vo.SwiperVO;
import com.allsparkstudio.zaixiyou.pojo.vo.WindowVO;

import java.util.List;

public interface RestaurantService {
    ResponseVO<List<WindowVO>> listWindows(Integer restaurantId);

    ResponseVO<List<SwiperVO>> listSwipers(Integer restaurantId);

    ResponseVO<List<FoodVO>> listFoods(Integer windowId, String token);

    ResponseVO like(Integer foodId, String token);

    ResponseVO disLike(Integer foodId, String token);
}
