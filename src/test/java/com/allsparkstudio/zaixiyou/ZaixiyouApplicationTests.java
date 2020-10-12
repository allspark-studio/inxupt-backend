package com.allsparkstudio.zaixiyou;

import com.allsparkstudio.zaixiyou.dao.FollowMapper;
import com.allsparkstudio.zaixiyou.dao.PostMapper;
import com.allsparkstudio.zaixiyou.dao.UserMapper;
import com.allsparkstudio.zaixiyou.pojo.form.ValidateForm;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.util.UUIDUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ZaixiyouApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    PostMapper postMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    UUIDUtils uuidUtils;

//    @Autowired
//    StringRedisTemplate stringRedisTemplate;

    @Test
    public void updateUserLiked() {

    }

    @Test
    public void test() {
        log.info("password: [{}]", DigestUtils.md5DigestAsHex("syspwd1".getBytes()));
    }

    @Test
    public void batchRegister() {

    }
}
