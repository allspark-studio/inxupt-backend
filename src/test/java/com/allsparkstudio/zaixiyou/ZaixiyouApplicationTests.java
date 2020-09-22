package com.allsparkstudio.zaixiyou;

import com.allsparkstudio.zaixiyou.dao.FollowMapper;
import com.allsparkstudio.zaixiyou.dao.PostMapper;
import com.allsparkstudio.zaixiyou.dao.UserMapper;
import com.allsparkstudio.zaixiyou.dao.UserPostMapper;
import com.allsparkstudio.zaixiyou.pojo.po.Post;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import com.allsparkstudio.zaixiyou.pojo.vo.UserLoginVO;
import com.allsparkstudio.zaixiyou.util.UUIDUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ZaixiyouApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    PostMapper postMapper;

    @Autowired
    UserPostMapper userPostMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    UUIDUtils uuidUtils;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void updateUserLiked() {
//        for (int i = 1; i<= 30; i++) {
//            synchronized (this) {
//                User user = userMapper.selectByPrimaryKey(i);
//                if (user == null) {
//                    continue;
//                }
//                Integer likeNum = 0;
//                List<Post> postList = postMapper.selectAllByAuthorId(user.getId());
//                for (Post post : postList) {
//                    likeNum += userPostMapper.countLikeByPostId(post.getId());
//                }
//                log.debug("user[{}], like:[{}]", i, likeNum);
//                user.setLikeNum(likeNum);
//                userMapper.updateLikeNum(user);
//            }
//        }
    }

    @Test
    public void test() {
//        log.info("password: [{}]", DigestUtils.md5DigestAsHex("zaixiyou1".getBytes()));
    }

    @Test
    public void batchRegister() throws IOException {

//        Gson gson = new Gson();
//
//        int num = 200;
//        String prefix = "ZXY";
//
//        for (int i = 2; i <= num; i++) {
//            if (i == 6) {
//                continue;
//            }
//            String phone;
//            if (i < 10) {
//                phone = prefix + "00" + i;
//            } else if (i < 100) {
//                phone = prefix + "0" + i;
//            } else {
//                phone = prefix + i;
//            }
//            stringRedisTemplate.opsForHash().put("code", phone, "100086");
//            HttpClient client = HttpClients.createDefault();
//            String registerUrl = "http://localhost:8080/user/register/validate";
//            HttpPost registerPost = new HttpPost(registerUrl);
//            Map<String, String> entity = new HashMap<>(2);
//            entity.put("phone", phone);
//            entity.put("code", "100086");
//            StringEntity stringEntity = new StringEntity(gson.toJson(entity));
//            stringEntity.setContentEncoding("UTF-8");
//            stringEntity.setContentType("application/json");
//
//            registerPost.setEntity(stringEntity);
//
//            client.execute(registerPost);
//        }
    }
}
