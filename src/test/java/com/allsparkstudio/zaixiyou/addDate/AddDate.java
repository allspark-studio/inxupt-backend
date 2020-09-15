package com.allsparkstudio.zaixiyou.addDate;

import com.allsparkstudio.zaixiyou.ZaixiyouApplicationTests;
import com.allsparkstudio.zaixiyou.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 陈帅
 * @date 2020/8/28
 */
@Slf4j
public class AddDate extends ZaixiyouApplicationTests {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Autowired
    UserService userService;

    @Test
    public void addUserWithFollower() {
//        for(int i = 150; i<1000; i++) {
//            ValidateForm form = new ValidateForm();
//            form.setPhone(String.valueOf(i));
//            form.setCode("100086");
//            ResponseVO<UserLoginVO> register = userService.register(form);
//            String token = register.getData().getToken();
//            for (int j = 34; j<34 + i;j++) {
//                userService.follow(j, token);
//            }
//        }
    }

    @Test
    public void addUser() {
//        ValidateForm form = new ValidateForm();
//        form.setPhone("18259234106");
//        form.setCode("195058");
//        ResponseVO<UserLoginVO> register = userService.register(form);
//        log.info(gson.toJson(register.getData()));
    }

    @Test
    public void addFollower() {
//        String token = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MTQxNDgyMjcsInN1YiI6NDAsImNyZWF0ZWQiOjE1OTg1OTYyMjc2NjB9.SMCzSiT4BothAa9UOuAoIdqNoe82jy4aS3-9aLYCyO0jVevX7OSEE4v3X_sAp8IPa6VdnS-IBrBtjwx4kfH_QA";
//        for (int i = 34; i<= 39; i++) {
//            userService.follow(i, token);
//        }
    }
}
