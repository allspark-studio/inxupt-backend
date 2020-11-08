package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.po.Food;
import com.allsparkstudio.zaixiyou.pojo.po.RestaurantSwiper;
import com.allsparkstudio.zaixiyou.pojo.po.UserFoodLike;
import com.allsparkstudio.zaixiyou.pojo.po.Window;
import com.allsparkstudio.zaixiyou.pojo.vo.FoodVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.pojo.vo.SwiperVO;
import com.allsparkstudio.zaixiyou.pojo.vo.WindowVO;
import com.allsparkstudio.zaixiyou.service.RestaurantService;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    RestaurantMapper restaurantMapper;

    @Autowired
    WindowMapper windowMapper;

    @Autowired
    RestaurantSwiperMapper restaurantSwiperMapper;

    @Autowired
    FoodMapper foodMapper;

    @Autowired
    UserFoodLikeMapper userFoodLikeMapper;

    @Autowired
    JWTUtils jwtUtils;


    @Override
    public ResponseVO<List<WindowVO>> listWindows(Integer restaurantId) {
        List<Window> windowList = windowMapper.selectByRestaurantId(restaurantId);
        List<WindowVO> windowVOList = new ArrayList<>();
        for (Window window : windowList) {
            WindowVO windowVO = new WindowVO();
            windowVO.setId(window.getId());
            windowVO.setName(window.getName());
            windowVO.setPhone(window.getPhone());
            windowVOList.add(windowVO);
        }
        return ResponseVO.success(windowVOList);
    }

    @Override
    public ResponseVO<List<SwiperVO>> listSwipers(Integer restaurantId) {
        List<RestaurantSwiper> swiperList = restaurantSwiperMapper.selectByRestaurantId(restaurantId);
        List<SwiperVO> swiperVOList = new ArrayList<>();
        for (RestaurantSwiper swiper : swiperList) {
            SwiperVO swiperVO = new SwiperVO();
            swiperVO.setImgUrl(swiper.getImgUrl());
            swiperVO.setWindowId(swiper.getWindowId());
            swiperVOList.add(swiperVO);
        }
        return ResponseVO.success(swiperVOList);
    }

    @Override
    public ResponseVO<List<FoodVO>> listFoods(Integer windowId, String token) {
        boolean login = false;
        Integer userId = null;
        if (jwtUtils.validateToken(token)) {
            login = true;
            userId = jwtUtils.getIdFromToken(token);
        }
        List<Food> foodList = foodMapper.selectByWindowId(windowId);
        List<FoodVO> foodVOList = new ArrayList<>();
        for (Food food : foodList) {
            FoodVO foodVO = new FoodVO();
            foodVO.setId(food.getId());
            foodVO.setImgUrl(food.getImage());
            foodVO.setLikedNum(food.getLikeNum());
            foodVO.setName(food.getName());
            foodVO.setPrice(food.getPrice());
            boolean isLiked = false;
            if (login) {
                UserFoodLike userFoodLike = userFoodLikeMapper.selectByUserIdAndFoodId(userId, food.getId());
                if (userFoodLike == null) {
                    isLiked = false;
                }else {
                    isLiked = userFoodLike.getState().equals(1);
                }
            }
            foodVO.setIsLiked(isLiked);
            foodVOList.add(foodVO);
        }
        return ResponseVO.success(foodVOList);
    }

    @Override
    public ResponseVO like(Integer foodId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        UserFoodLike userFoodLike = userFoodLikeMapper.selectByUserIdAndFoodId(userId, foodId);
        if (userFoodLike == null) {
            userFoodLike = new UserFoodLike();
            userFoodLike.setUserId(userId);
            userFoodLike.setFoodId(foodId);
            userFoodLike.setState(1);
            int result = userFoodLikeMapper.insertSelective(userFoodLike);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }else {
            userFoodLike.setState(1);
            int result = userFoodLikeMapper.updateState(userFoodLike);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        Food food = foodMapper.selectByPrimaryKey(foodId);
        food.setLikeNum(food.getLikeNum() + 1);
        foodMapper.updateLikeNum(food);
        return ResponseVO.success();
    }

    @Override
    public ResponseVO disLike(Integer foodId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        UserFoodLike userFoodLike = userFoodLikeMapper.selectByUserIdAndFoodId(userId, foodId);
        if (userFoodLike == null) {
            userFoodLike = new UserFoodLike();
            userFoodLike.setUserId(userId);
            userFoodLike.setFoodId(foodId);
            userFoodLike.setState(0);
            int result = userFoodLikeMapper.insertSelective(userFoodLike);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }else {
            userFoodLike.setState(0);
            int result = userFoodLikeMapper.updateState(userFoodLike);
            if (result != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        return ResponseVO.success();
    }
}
