package com.allsparkstudio.zaixiyou.consumer;

import com.allsparkstudio.zaixiyou.ZaixiyouApplicationTests;
import com.allsparkstudio.zaixiyou.dao.PostMapper;
import com.allsparkstudio.zaixiyou.dao.UserMapper;
import com.allsparkstudio.zaixiyou.pojo.po.Post;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Slf4j
public class MySQL2ESConsumerTest extends ZaixiyouApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    PostMapper postMapper;

    @Test
    public void addPost() {

        // TODO: p14 21:01 批量插入数据到ES
//        Post post = postMapper.selectByPrimaryKey(256);
//        rabbitTemplate.convertAndSend("MySQL2ESPostExchange", "add", post);
    }

    @Test
    public void updatePost() {

//        Post post = postMapper.selectByPrimaryKey(254);
//        post.setBody("这是更新之后的数据数据数据数据");
//        rabbitTemplate.convertAndSend("MySQL2ESPostExchange", "update", post);
    }

    @Test
    public void deletePost() {
//        Post post = postMapper.selectByPrimaryKey(254);
//        post.setBody("这是更新之后的数据数据数据数据");
//        rabbitTemplate.convertAndSend("MySQL2ESPostExchange", "delete", post);
    }

    @Test
    public void addUser() {
//        User user = new User();
//        user.setAvatarUrl("avatarurl");
//        user.setId(12);
//        user.setNickname("nickname");
//        user.setGender(0);
//        user.setDescription("description");
//        user.setInsertableCoins(0);
//        user.setExchangeableCoins(0);
//        user.setPhone("phone");
//        user.setMajor("major");
//        user.setGrade("grade");
//        user.setPassword("password");
//        user.setLevel(1);
//        user.setExperience(100);
//        user.setCustomAppColor("color");
//        user.setState(0);
//        user.setRegisterTime(new Date());
//        user.setLastActiveTime("activeTime");
//        user.setUserpageBgImgUrl("url");
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void addCircle() {
    }

    @Test
    public void updateCircle() {
    }

    @Test
    public void deleteCircle() {
    }
}