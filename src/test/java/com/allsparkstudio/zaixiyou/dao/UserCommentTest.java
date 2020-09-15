package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.ZaixiyouApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class UserCommentTest extends ZaixiyouApplicationTests {

    @Autowired
    UserCommentMapper userCommentMapper;

    @Test
    public void countLikeByUserId() {
//        int result = userCommentMapper.countLikeByUserId(1);
//        log.info("result = [{}]", result);
//        assert result == 3;
    }
}
