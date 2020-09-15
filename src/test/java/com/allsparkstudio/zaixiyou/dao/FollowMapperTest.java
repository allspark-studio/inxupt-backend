package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.ZaixiyouApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class FollowMapperTest extends ZaixiyouApplicationTests {

    @Autowired
    FollowMapper followMapper;

    @Test
    public void testCountByUserId() {
//        int result = followMapper.countByUserId(1);
//        log.info("result: {}", result);
    }

    @Test
    public void testCountByFollowedUserId() {
//        int result = followMapper.countByFollowedUserId(3);
//        log.info("result: {}", result);
    }

    @Test
    public void isFollowed() {
//        Boolean result = followMapper.isFollowed(1, 4);
//        log.info("result : [{}]", result);
//        assert !result;
    }

    @Test
    public void updateFollow() {
//        Follow follow = new Follow();
//        follow.setUserId(1);
//        follow.setFollowedUserId(3);
//        follow.setStatus(true);
//        int result = followMapper.updateFollow(follow);
//        log.info("result : [{}]", result);
//        assert result == 2;
    }
}
