package com.allsparkstudio.zaixiyou.controller;


import com.allsparkstudio.zaixiyou.pojo.vo.FoodVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.pojo.vo.SwiperVO;
import com.allsparkstudio.zaixiyou.pojo.vo.WindowVO;
import com.allsparkstudio.zaixiyou.service.RestaurantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "食堂")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @GetMapping("/restaurant/{restaurantId}/windows")
    @ApiOperation("窗口列表")
    public ResponseVO<List<WindowVO>> listWindows(@PathVariable("restaurantId") Integer restaurantId) {
        return restaurantService.listWindows(restaurantId);
    }

    @GetMapping("/restaurant/{restaurantId}/swipers")
    @ApiOperation("轮播图")
    public ResponseVO<List<SwiperVO>> listSwipers(@PathVariable("restaurantId") Integer restaurantId) {
        return restaurantService.listSwipers(restaurantId);
    }

    @GetMapping("/window/{windowId}/foods")
    @ApiOperation("食物列表")
    public ResponseVO<List<FoodVO>> listFoods(@PathVariable("windowId") Integer windowId,
                                        @RequestHeader("token") String token) {
        return restaurantService.listFoods(windowId, token);
    }

    @PostMapping("/food/{foodId}/like")
    @ApiOperation("点赞菜品")
    public ResponseVO like(@PathVariable("foodId") Integer foodId,
                           @RequestHeader("token") String token) {
        return restaurantService.like(foodId, token);
    }

    @DeleteMapping("/food/{foodId}/like")
    @ApiOperation("取消点赞")
    public ResponseVO disLike(@PathVariable("foodId") Integer foodId,
                              @RequestHeader("token") String token) {
        return restaurantService.disLike(foodId, token);
    }
}