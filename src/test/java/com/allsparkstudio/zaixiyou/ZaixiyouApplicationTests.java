package com.allsparkstudio.zaixiyou;

import com.allsparkstudio.zaixiyou.dao.FollowMapper;
import com.allsparkstudio.zaixiyou.dao.UserMapper;
import com.allsparkstudio.zaixiyou.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ZaixiyouApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    UUIDUtils uuidUtils;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void load() {
//        redisTemplate.opsForSet().add("activeUserIdSet", String.valueOf(1));
//        redisTemplate.opsForSet().add("activeUserIdSet", String.valueOf(2));
//        redisTemplate.opsForSet().add("activeUserIdSet", String.valueOf(2));
    }

    @Test
    public void test() {
        Boolean activeUserIdSet = redisTemplate.opsForSet().isMember("activeUserIdSet", String.valueOf(1));
        log.info("是否存在:[{}]", activeUserIdSet);
    }

}
