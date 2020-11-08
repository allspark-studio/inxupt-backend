package com.allsparkstudio.zaixiyou.service;

import com.allsparkstudio.zaixiyou.pojo.form.ValidateForm;
import com.allsparkstudio.zaixiyou.pojo.form.PasswordForm;
import com.allsparkstudio.zaixiyou.pojo.form.UpdateUserForm;
import com.allsparkstudio.zaixiyou.pojo.vo.*;
import com.github.pagehelper.PageInfo;

/**
 * @author 陈帅
 * @date 2020/7/16
 */
public interface UserService {

    /**
     * 通过用户名或手机号及密码登录
     * @param identifier 手机号
     * @param password 密码
     */
    ResponseVO<UserLoginVO> loginByPassword(String identifier, String password);

    /**
     * 通过手机号和验证码登录
     * @param phone 手机号
     * @param code 验证码
     * @return 返回成功或失败信息
     */
    ResponseVO<UserLoginVO> loginByCode(String phone, String code);

    /**
     * 注册
     * @param validateForm  注册时提交的表单
     * @return 返回注册成功或者失败信息
     */
    ResponseVO<UserLoginVO> register(ValidateForm validateForm);

    /**
     * 获取验证码
     * @param phone  输入的手机号
     * @return  发送验证码并存入Redis
     */
    ResponseVO getCode(String phone);

    /**
     * "我的"页面
     */
    ResponseVO<MyPageVO> myPage(String token);

    /**
     * 其他人的个人信息页
     * @param uid 他的id
     */
    ResponseVO<HisPageVO> hisPage(Integer uid, String token);

    /**
     * 关注
     */
    ResponseVO follow(Integer userId, String token);

    /**
     * 取消关注
     */
    ResponseVO disFollow(Integer userId, String token);

    /**
     * 更新个人信息
     */
    ResponseVO updateUser(UpdateUserForm form, String token);

    /**
     * 根据id展示用户的粉丝列表
     */
    ResponseVO<PageInfo> listFans(Integer userId, Integer pageNum, Integer pgeSize, String token);

    /**
     * 根据id展示用户的关注列表
     */
    ResponseVO<PageInfo> listFollows(Integer userId, Integer pageNum, Integer pageSize, String token);

    /**
     * 用户设置/修改密码
     */
    ResponseVO<UserLoginVO> setPassword(PasswordForm passwordForm, String token);

    /**
     * 修改密码时验证手机
     */
    ResponseVO<UserLoginVO> validate(ValidateForm validateForm);

    ResponseVO getAvatarAndNickname(Integer userId);

    ResponseVO updateAvatar(String token, String url);

    ResponseVO updateBackground(String token, String url);

    ResponseVO getBackground(String token);
}
