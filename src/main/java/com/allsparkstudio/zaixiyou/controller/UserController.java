package com.allsparkstudio.zaixiyou.controller;

import com.allsparkstudio.zaixiyou.enums.PostTypeEnum;
import com.allsparkstudio.zaixiyou.enums.UserContentStateEnum;
import com.allsparkstudio.zaixiyou.pojo.form.*;
import com.allsparkstudio.zaixiyou.pojo.vo.*;
import com.allsparkstudio.zaixiyou.service.PostService;
import com.allsparkstudio.zaixiyou.service.UserService;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 陈帅
 * @date 2020/7/16
 */
@Slf4j
@RestController
@Api(tags = "用户")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    JWTUtils jwtUtils;

    /**
     * 发送验证码
     */
    @ApiOperation("获取验证码")
    @GetMapping("/user/code")
    public ResponseVO getCode(@ApiParam("手机号") @RequestParam String phone) {
        return userService.getCode(phone);
    }

    /**
     * 通过手机验证码登录
     */
    @ApiOperation("通过手机号和验证码登录")
    @PostMapping("/user/loginByCode")
    public ResponseVO loginByCode(@ApiParam("登录表单：手机号,验证码") @RequestBody LoginByCodeForm loginForm) {
        return userService.loginByCode(loginForm.getPhone(), loginForm.getCode());
    }

    /**
     * 通过密码登录
     */
    @ApiOperation("通过手机号和密码登录")
    @PostMapping("/user/loginByPassword")
    public ResponseVO login(@RequestBody LoginByPasswordForm loginForm) {
        return userService.loginByPassword(loginForm.getPhone(), loginForm.getPassword());
    }

    /**
     * 设置/修改密码
     */
    @ApiOperation("设置/修改密码")
    @PutMapping("/user/password")
    public ResponseVO<UserLoginVO> setPassword(@RequestBody PasswordForm passwordForm,
                                               @RequestHeader(value = "token", required = false) String token) {
        return userService.setPassword(passwordForm, token);
    }

    /**
     * 注册
     *
     * @param validateForm 客户端填写的注册表单
     */
    @ApiOperation("注册时验证手机号和验证码")
    @PostMapping("/user/register/validate")
    public ResponseVO<UserLoginVO> register(@RequestBody ValidateForm validateForm) {
        return userService.register(validateForm);
    }

    /**
     * 修改密码时验证手机号
     */
    @ApiOperation("修改密码时验证手机号和验证码")
    @PostMapping("/user/password/validate")
    public ResponseVO<UserLoginVO> validate(@RequestBody ValidateForm validateForm) {
        return userService.validate(validateForm);
    }

    /**
     * 我的个人主页
     */
    @ApiOperation("我的个人主页")
    @GetMapping("/user/page")
    public ResponseVO<MyPageVO> myPage(@RequestHeader(value = "token", required = false) String token) {
        return userService.myPage(token);
    }

    /**
     * 其他用户的个人主页
     *
     * @param userId 该用户的id
     */
    @ApiOperation("查看别人的主页")
    @GetMapping("/user/{userId}/page")
    public ResponseVO<HisPageVO> hisPage(@ApiParam("要查看的用户的id") @PathVariable("userId") Integer userId,
                                         @RequestHeader(value = "token", required = false) String token) {
        return userService.hisPage(userId, token);
    }

    @PutMapping("/user")
    @ApiOperation("修改我的个人信息")
    public ResponseVO updateUser(@ApiParam("用户提交的表单") @RequestBody UpdateUserForm form,
                                 @RequestHeader(value = "token", required = false) String token) {
        return userService.updateUser(form, token);
    }

    /**
     * 关注用户
     */
    @PostMapping("/user/follow/{userId}")
    @ApiOperation("关注用户")
    public ResponseVO follow(@PathVariable("userId") Integer userId,
                             @RequestHeader(value = "token", required = false) String token) {
        return userService.follow(userId, token);
    }

    /**
     * 取消关注
     */
    @DeleteMapping("/user/follow/{userId}")
    @ApiOperation("取消关注用户")
    public ResponseVO disFollow(@PathVariable("userId") Integer userId,
                                @RequestHeader(value = "token", required = false) String token) {
        return userService.disFollow(userId, token);
    }

    @GetMapping("/user/{userId}/fans")
    @ApiOperation("用户粉丝列表")
    public ResponseVO<PageInfo> listFans(@PathVariable("userId") Integer userId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                         @RequestHeader(value = "token", required = false) String token) {
        return userService.listFans(userId, pageNum, pageSize, token);
    }

    @GetMapping("/user/{userId}/follows")
    @ApiOperation("用户关注列表")
    public ResponseVO<PageInfo> listFollows(@PathVariable("userId") Integer userId,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            @RequestHeader(value = "token", required = false) String token) {
        return userService.listFollows(userId, pageNum, pageSize, token);
    }

    @GetMapping("/user/{userId}/posts")
    @ApiOperation("用户动态")
    public ResponseVO<PageInfo> listPosts(@PathVariable("userId") Integer userId,
                                          @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                          @RequestParam(required = false, defaultValue = "1") Integer sortedBy,
                                          @RequestHeader(value = "token", required = false) String token) {
        return postService.listAll(null, userId, null, PostTypeEnum.POST.getCode(), null, token, pageNum, pageSize, sortedBy);
    }

    @GetMapping("/user/{userId}/articles")
    @ApiOperation("用户文章")
    public ResponseVO<PageInfo> listArticles(@PathVariable("userId") Integer userId,
                                             @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false, defaultValue = "1") Integer sortedBy,
                                             @RequestHeader(value = "token", required = false) String token) {
        return postService.listAll(null, userId, null, PostTypeEnum.ARTICLE.getCode(), null, token, pageNum, pageSize, sortedBy);
    }

    @GetMapping("/user/favorites/posts")
    @ApiOperation("用户收藏的帖子")
    public ResponseVO<PageInfo> listFavoritePosts(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                  @RequestParam(required = false, defaultValue = "1") Integer sortedBy,
                                                  @RequestHeader(value = "token", required = false) String token) {
        return postService.listAll(null, null, null, PostTypeEnum.POST.getCode(), UserContentStateEnum.FAVORITE, token, pageNum, pageSize, sortedBy);
    }

    @GetMapping("/user/favorites/articles")
    @ApiOperation("用户收藏的文章")
    public ResponseVO<PageInfo> listFavoriteArticles(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false, defaultValue = "1") Integer sortedBy,
                                                     @RequestHeader(value = "token", required = false) String token) {
        return postService.listAll(null, null, null, PostTypeEnum.ARTICLE.getCode(), UserContentStateEnum.FAVORITE, token, pageNum, pageSize, sortedBy);
    }

}
