package com.allsparkstudio.zaixiyou;

import com.allsparkstudio.zaixiyou.util.entity.Code2SessionResponse;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: Marble
 * @Date: 2021/6/3 21:30
 * @Description: 测试CetUtils
 */
@SpringBootTest
public class test {

    @Test
    public void TestGson() {
        Code2SessionResponse result = gson.fromJson("{\"session_key\":\"j5id6DGZ6EAFCzMtRXByPA==\",\"openid\":\"obPWI5WfV1l0NBp5aZhFSF5wOPTk\"}", Code2SessionResponse.class);
        System.out.println(result);
    }

    private static final Gson gson = new Gson();
}
