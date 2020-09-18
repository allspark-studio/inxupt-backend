package com.allsparkstudio.zaixiyou;

import com.allsparkstudio.zaixiyou.dao.FollowMapper;
import com.allsparkstudio.zaixiyou.dao.PostMapper;
import com.allsparkstudio.zaixiyou.dao.UserMapper;
import com.allsparkstudio.zaixiyou.dao.UserPostMapper;
import com.allsparkstudio.zaixiyou.pojo.po.Post;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import com.allsparkstudio.zaixiyou.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


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
    public void load() {
//        for (int i = 1; i<= 30; i++) {
//            synchronized (this) {
//                User user = userMapper.selectByPrimaryKey(i);
//                if (user == null) {
//                    continue;
//                }
//                Integer likeNum = 0;
//                List<Post> postList = postMapper.selectByUserIdAndType(user.getId(), 1);
//                List<Post> articleList = postMapper.selectByUserIdAndType(user.getId(), 2);
//                for (Post post : postList) {
//                    likeNum += userPostMapper.countLikeByPostId(post.getId());
//                }
//                for (Post article : articleList) {
//                    likeNum += userPostMapper.countLikeByPostId(article.getId());
//                }
//                log.debug("user[{}], like:[{}]", i, likeNum);
//                user.setLikeNum(likeNum);
//                userMapper.updateLikeNum(user);
//            }
//        }
    }

    @Test
    public void test() {
//        Boolean activeUserIdSet = redisTemplate.opsForSet().isMember("activeUserIdSet", String.valueOf(1));
//        log.info("是否存在:[{}]", activeUserIdSet);
    }

}
