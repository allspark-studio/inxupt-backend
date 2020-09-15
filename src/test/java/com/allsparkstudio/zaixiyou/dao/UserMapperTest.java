package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.ZaixiyouApplicationTests;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import com.allsparkstudio.zaixiyou.util.UUIDUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 陈帅
 * @date 2020/7/17
 */
@Slf4j
public class UserMapperTest extends ZaixiyouApplicationTests {

    @Autowired
    UUIDUtils uuidUtils;

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    private UserMapper userMapper;

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testInsert() {
//        User user = new User();
//        user.setPhone("123456");
//        user.setAvatarUrl(DefaultSettingConsts.DEFAULT_USER_AVATAR_URL_2);
//        user.setDescription(DefaultSettingConsts.DEFAULT_USER_DESCRIPTION);
//        user.setNickname("邮友_" + uuidUtils.generateShortUUID());
//        int result = userMapper.insertSelective(user);
//        if (result != 1) {
//            log.error("user表插入失败，phone:[{}]", "123456");
//        }
//        String token = jwtUtils.generateToken(user);
//        log.info("用户注册成功，phone:[{}]", "123456");
//        UserLoginVO userLoginVO = new UserLoginVO();
//        userLoginVO.setToken(token);
//        userLoginVO.setUserId(user.getId());
//        log.info("userId:[{}]", user.getId());
////        log.info();
    }

    @Test
    public void testSelectByOpenid() {
//        User user = userMapper.selectByPhone("phone");
//       log.info(user.toString());
    }

}
