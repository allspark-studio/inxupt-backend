package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.consts.DefaultSettingConsts;
import com.allsparkstudio.zaixiyou.consts.ExperienceConst;
import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.enums.PostTypeEnum;
import com.allsparkstudio.zaixiyou.enums.RemindActionEnum;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.enums.UserStateEnum;
import com.allsparkstudio.zaixiyou.pojo.form.ValidateForm;
import com.allsparkstudio.zaixiyou.pojo.form.PasswordForm;
import com.allsparkstudio.zaixiyou.pojo.form.UpdateUserForm;
import com.allsparkstudio.zaixiyou.pojo.po.EventRemind;
import com.allsparkstudio.zaixiyou.pojo.po.Follow;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import com.allsparkstudio.zaixiyou.pojo.vo.*;
import com.allsparkstudio.zaixiyou.service.UserService;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import com.allsparkstudio.zaixiyou.util.SMSUtils;
import com.allsparkstudio.zaixiyou.util.UUIDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author AlkaidChen
 * @date 2020/7/16
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final static String CODE_REDIS_KEY_TEMPLATE = "phone_%s";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private UserPostFavoriteMapper userPostFavoriteMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UUIDUtils uuidUtils;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseVO<UserLoginVO> loginByPassword(String phone, String password) {
        User user = userMapper.selectByPhone(phone);
        if (user == null) {
            return ResponseVO.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        if (UserStateEnum.BANNED.getCode().equals(user.getState())) {
            return ResponseVO.error(ResponseEnum.BANNED);
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PASSWORD);
        }
        if (!user.getPassword().equalsIgnoreCase(DigestUtils.md5DigestAsHex(
                password.getBytes(StandardCharsets.UTF_8)))) {
            return ResponseVO.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        String token = jwtUtils.generateToken(user);
        log.info("用户通过密码登录成功，phone: [{}]", phone);
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(token);
        userLoginVO.setUserId(user.getId());
        return ResponseVO.success(userLoginVO, "登录成功");
    }

    @Override
    public ResponseVO<UserLoginVO> loginByCode(String phone, String code) {
        User user = userMapper.selectByPhone(phone);
        if (user == null) {
            return ResponseVO.error(ResponseEnum.USER_NOT_REGISTER);
        }
        if (UserStateEnum.BANNED.getCode().equals(user.getState())) {
            return ResponseVO.error(ResponseEnum.BANNED);
        }
        if (!smsUtils.validateCode(phone, code)) {
            return ResponseVO.error(ResponseEnum.CODE_NOT_MATCH);
        }
        String token = jwtUtils.generateToken(user);
        log.info("用户通过验证码登录成功: phone:[{}], code:[{}]", phone, code);
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(token);
        userLoginVO.setUserId(user.getId());
        return ResponseVO.success(userLoginVO, "登录成功");
    }

    /**
     * 注册
     *
     * @param validateForm 注册时提交的表单
     * @return 注册成功或失败信息
     */
    @Override
    public ResponseVO<UserLoginVO> register(ValidateForm validateForm) {
        User user = userMapper.selectByPhone(validateForm.getPhone());
        // 校验手机号是否正确
        if (user != null) {
            return ResponseVO.error(ResponseEnum.PHONE_EXIST);
        }
        // 校验验证码
        if (!smsUtils.validateCode(validateForm.getPhone(), validateForm.getCode())) {
            return ResponseVO.error(ResponseEnum.CODE_NOT_MATCH);
        }
        user = new User();
        user.setPhone(validateForm.getPhone());
        user.setAvatarUrl(DefaultSettingConsts.getDefaultAvatar());
        user.setDescription(DefaultSettingConsts.DEFAULT_USER_DESCRIPTION);
        user.setBackgroundUrl(DefaultSettingConsts.DEFAULT_USERPAGE_BACKGROUND_URL);
        user.setNickname("邮友_" + uuidUtils.generateShortUUID());
        int result = userMapper.insertSelective(user);
        if (result != 1) {
            log.error("user表插入失败，phone:[{}]", validateForm.getPhone());
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        // 通过MQ同步数据到ES
        rabbitTemplate.convertAndSend("MySQL2ESUserExchange", "add", user);
        String token = jwtUtils.generateToken(user);
        log.info("用户注册成功，phone:[{}], token:[{}], userId:[{}]", validateForm.getPhone(), token, user.getId());
        // 使用MQ延迟更新当日注册量数
        rabbitTemplate.convertAndSend("dailyStatisticsExchange","userRegister", user);
        log.debug("生产者routerKey=userRegister发送消息");
        // 构造VO对象
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(token);
        userLoginVO.setUserId(user.getId());
        return ResponseVO.success(userLoginVO, "登录成功");
    }

    /**
     * 获取验证码并存入redis，并在一分钟内禁止再次获取验证码
     *
     * @param phone 输入的手机号
     * @return 发送验证码并存入redis
     */
    @Override
    public ResponseVO getCode(String phone) {
        // 禁止一分钟内重复发送验证码
        String key = String.format(CODE_REDIS_KEY_TEMPLATE, phone);
        if (stringRedisTemplate.hasKey(key)) {
            return ResponseVO.error(ResponseEnum.CODE_SENT_FREQUENTLY);
        }
        String code = smsUtils.sendCode(phone);
        if (code != null) {
            stringRedisTemplate.opsForValue().set(key, code);
            stringRedisTemplate.expire(key, 1, TimeUnit.MINUTES);
            log.info("已成功发送验证码，phone: [{}]", phone);
            return ResponseVO.success("验证码已发送");
        }
        log.error("发送验证码失败，phone：[{}]", phone);
        return ResponseVO.error(ResponseEnum.ERROR);
    }

    @Override
    public ResponseVO<MyPageVO> myPage(String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer uid = jwtUtils.getIdFromToken(token);
        User user = userMapper.selectByPrimaryKey(uid);
        // 构建VO对象
        MyPageVO myPageVO = new MyPageVO();
        myPageVO.setNickname(user.getNickname());
        myPageVO.setAvatarUrl(user.getAvatarUrl());
        myPageVO.setLevel(user.getLevel());
        myPageVO.setGender(user.getGender());
        myPageVO.setGrade(user.getGrade());
        myPageVO.setMajor(user.getMajor());
        myPageVO.setAccountAuth(Arrays.asList(user.getAccountAuth().split(";")));
        myPageVO.setSignCoinsNum(user.getInsertableCoins());
        myPageVO.setExchangeCoinsNum(user.getExchangeableCoins());
        myPageVO.setExperience(user.getExperience());
        myPageVO.setNeedExperience(ExperienceConst.getExpByLv(user.getLevel()));
        myPageVO.setDescription(user.getDescription());
        Integer postsNum = postMapper.countByAuthorIdAndType(uid, PostTypeEnum.POST.getCode());
        Integer articleNum = postMapper.countByAuthorIdAndType(uid, PostTypeEnum.ARTICLE.getCode());
        Integer newsNum = postsNum + articleNum;
        myPageVO.setNewsNum(newsNum);
        myPageVO.setFansNum(followMapper.countByFollowedUserId(uid));
        myPageVO.setFollowNum(followMapper.countByUserId(uid));
        Integer favoriteNum = userPostFavoriteMapper.countByUserId(uid);
        myPageVO.setFavoriteNum(favoriteNum);

        return ResponseVO.success(myPageVO);
    }

    @Override
    public ResponseVO<HisPageVO> hisPage(Integer uid, String token) {
        User he = userMapper.selectByPrimaryKey(uid);
        if (he == null) {
            log.error("请求的用户信息不存在, userId:[{}]", uid);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "用户不存在");
        }
        boolean login = false;
        if (!StringUtils.isEmpty(token) && jwtUtils.validateToken(token)) {
            login = true;
        }
        // 构建VO对象
        HisPageVO hisPageVO = new HisPageVO();
        hisPageVO.setHisId(uid);
        hisPageVO.setNickname(he.getNickname());
        hisPageVO.setAvatarUrl(he.getAvatarUrl());
        hisPageVO.setBackgroundUrl(he.getBackgroundUrl());
        hisPageVO.setDescription(he.getDescription());
        hisPageVO.setGender(he.getGender());
        hisPageVO.setGrade(he.getGrade());
        hisPageVO.setMajor(he.getMajor());
        hisPageVO.setLevel(he.getLevel());
        hisPageVO.setLikedNum(he.getLikeNum());
        hisPageVO.setAccountAuth(Arrays.asList(he.getAccountAuth().split(";")));
        Integer fansNum = followMapper.countByFollowedUserId(uid);
        hisPageVO.setFansNum(fansNum);
        Integer followNum = followMapper.countByUserId(uid);
        hisPageVO.setFollowNum(followNum);
        boolean isFollowed = false;
        if (login) {
            Integer myId = jwtUtils.getIdFromToken(token);
            isFollowed = followMapper.isFollowed(myId, uid);
        }
        hisPageVO.setFollowed(isFollowed);
        Integer postsNum = postMapper.countByAuthorIdAndType(uid, PostTypeEnum.POST.getCode());
        hisPageVO.setNewsNum(postsNum);
        Integer articlesNum = postMapper.countByAuthorIdAndType(uid, PostTypeEnum.ARTICLE.getCode());
        hisPageVO.setArticleNum(articlesNum);
        return ResponseVO.success(hisPageVO);
    }

    /**
     * 关注
     * @param userId 被关注的用户id
     * @param token  客户端传入的token
     */
    @Override
    public ResponseVO follow(Integer userId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED, "身份校验失败");
        }
        Integer myId = jwtUtils.getIdFromToken(token);
        if (myId.equals(userId)) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "不能关注自己哦");
        }
        Follow follow = followMapper.selectBy2UserId(myId, userId);
        if (follow == null) {
            follow = new Follow();
            follow.setUserId(myId);
            follow.setFollowedUserId(userId);
            follow.setState(true);
            int result = followMapper.insertSelective(follow);
            if (result != 1) {
                log.error("关注用户时出现错误,'follower'表更新失败");
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        follow.setFollowedUserId(userId);
        follow.setState(true);
        int result = followMapper.updateByPrimaryKeySelective(follow);
        if (result != 1) {
            log.error("关注用户时出现错误,'follower'表更新失败");
        }
        // MQ更新用户经验
        rabbitTemplate.convertAndSend("dailyStatisticsExchange", "getFollowed", userId);
        // MQ通知用户被关注
        EventRemind remind = new EventRemind();
        remind.setAction(RemindActionEnum.FOLLOW.getCode());
        remind.setSenderId(myId);
        remind.setReceiveId(userId);
        rabbitTemplate.convertAndSend("eventRemind", remind);
        return ResponseVO.success();
    }

    /**
     * 取消关注
     *
     * @param userId 被关注的用户id
     * @param token  客户端传入的token
     */
    @Override
    public ResponseVO disFollow(Integer userId, String token) {

        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED, "身份校验失败");
        }
        Integer myId = jwtUtils.getIdFromToken(token);
        if (myId.equals(userId)) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "不能取关自己哦");
        }
        Follow follow = followMapper.selectBy2UserId(myId, userId);
        if (follow == null) {
            follow = new Follow();
            follow.setUserId(myId);
            follow.setFollowedUserId(userId);
            follow.setState(false);
            int result = followMapper.insertSelective(follow);
            if (result != 1) {
                log.error("取消关注用户时出现错误,'follower'表更新失败");
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }
        follow.setFollowedUserId(userId);
        follow.setState(false);
        int result = followMapper.updateByPrimaryKeySelective(follow);
        if (result != 1) {
            log.error("取消关注用户时出现错误,'follower'表更新失败");
        }
        return ResponseVO.success();
    }

    /**
     * 更新个人信息
     *
     * @param form  更新个人信息表单
     * @param token token
     */
    @Override
    public ResponseVO updateUser(UpdateUserForm form, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer id = jwtUtils.getIdFromToken(token);
        User user = userMapper.selectByPrimaryKey(id);
        user.setNickname(form.getNickName());
        user.setDescription(form.getDescription());
        user.setGender(form.getGender());
        user.setGrade(form.getGrade());
        user.setMajor(form.getMajor());
        int result = userMapper.updateByPrimaryKeySelective(user);
        if (result != 1) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        // 通过MQ同步数据到ES
        rabbitTemplate.convertAndSend("MySQL2ESUserExchange", "update", user);
        return ResponseVO.success();
    }

    @Override
    public ResponseVO<PageInfo> listFans(Integer userId, Integer pageNum, Integer pageSize, String token) {
        Integer myId = null;
        boolean login = false;
        if (!StringUtils.isEmpty(token) && jwtUtils.validateToken(token)) {
            myId = jwtUtils.getIdFromToken(token);
            login = true;
        }
        List<UserVO> fansVOList = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        List<User> fansList = userMapper.selectFans(userId);
        for (User fan : fansList) {
            UserVO fansVO = new UserVO();
            fansVO.setId(fan.getId());
            fansVO.setAvatarUrl(fan.getAvatarUrl());
            fansVO.setDescription(fan.getDescription());
            fansVO.setNickName(fan.getNickname());
            fansVO.setAccountAuth(Arrays.asList(fan.getAccountAuth().split(";")));
            fansVO.setSelected(false);
            if (login) {
                fansVO.setFollowed(followMapper.isFollowed(myId, fan.getId()));
            } else {
                fansVO.setFollowed(false);
            }
            fansVOList.add(fansVO);
        }
        PageInfo pageInfo = new PageInfo(fansList);
        pageInfo.setList(fansVOList);
        return ResponseVO.success(pageInfo);
    }

    @Override
    public ResponseVO<PageInfo> listFollows(Integer userId, Integer pageNum, Integer pageSize, String token) {
        boolean login = false;
        Integer myId = null;
        if ((!StringUtils.isEmpty(token)) && (jwtUtils.validateToken(token))) {
            myId = jwtUtils.getIdFromToken(token);
            login = true;
        }
        List<UserVO> fansVOList = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        List<User> followersList = userMapper.selectFollowers(userId);
        for (User follower : followersList) {
            UserVO fansVO = new UserVO();
            fansVO.setId(follower.getId());
            fansVO.setAvatarUrl(follower.getAvatarUrl());
            fansVO.setDescription(follower.getDescription());
            fansVO.setNickName(follower.getNickname());
            fansVO.setAccountAuth(Arrays.asList(follower.getAccountAuth().split(";")));
            fansVO.setSelected(false);
            if (login) {
                fansVO.setFollowed(followMapper.isFollowed(myId, follower.getId()));
            } else {
                fansVO.setFollowed(false);
            }
            fansVOList.add(fansVO);
        }
        PageInfo pageInfo = new PageInfo<>(followersList);
        pageInfo.setList(fansVOList);
        return ResponseVO.success(pageInfo);
    }

    @Override
    public ResponseVO<UserLoginVO> setPassword(PasswordForm passwordForm, String token) {
        if (!(passwordForm.getPassword() != null &&
                passwordForm.getPassword().equals(passwordForm.getCheckPassword()))) {
            return ResponseVO.error(ResponseEnum.INCONSISTENT_PASSWORDS);
        }
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        int id = jwtUtils.getIdFromToken(token);
        User user = userMapper.selectByPrimaryKey(id);
        user.setPassword(DigestUtils.md5DigestAsHex(passwordForm.getPassword().getBytes()));
        int result = userMapper.updateByPrimaryKeySelective(user);
        if (result != 1) {
            log.error("设置/修改密码时出错,数据库表'user'更新失败, userId:[{}]", id);
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        return ResponseVO.success();
    }

    @Override
    public ResponseVO<UserLoginVO> validate(ValidateForm validateForm) {
        User user = userMapper.selectByPhone(validateForm.getPhone());
        // 校验手机号是否正确
        if (user == null) {
            return ResponseVO.error(ResponseEnum.USER_NOT_REGISTER);
        }
        // 校验验证码
        if (!smsUtils.validateCode(validateForm.getPhone(), validateForm.getCode())) {
            return ResponseVO.error(ResponseEnum.CODE_NOT_MATCH);
        }
        String token = jwtUtils.generateToken(user);
        log.info("用户校验成功，phone:[{}], token:[{}]", validateForm.getPhone(), token);
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(token);
        userLoginVO.setUserId(user.getId());
        return ResponseVO.success(userLoginVO);
    }

    @Override
    public ResponseVO getAvatarAndNickname(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "用户不存在");
        }
        AvatarAndNicknameVO avatarAndNicknameVO = new AvatarAndNicknameVO();
        avatarAndNicknameVO.setAvatar(user.getAvatarUrl());
        avatarAndNicknameVO.setNickname(user.getNickname());
        return ResponseVO.success(avatarAndNicknameVO);
    }

    @Override
    public ResponseVO updateAvatar(String token, String url) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        User user = userMapper.selectByPrimaryKey(userId);
        user.setAvatarUrl(url);
        int result = userMapper.updateAvatar(user);
        if (result == 0) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        return ResponseVO.success();
    }

    @Override
    public ResponseVO updateBackground(String token, String url) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        User user = userMapper.selectByPrimaryKey(userId);
        user.setBackgroundUrl(url);
        int result = userMapper.updateBackground(user);
        if (result == 0) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        return ResponseVO.success();
    }

    @Override
    public ResponseVO getBackground(String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        User user = userMapper.selectByPrimaryKey(userId);
        String backgroundUrl = user.getBackgroundUrl();
        return ResponseVO.success(backgroundUrl);
    }
}
