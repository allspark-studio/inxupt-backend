package com.allsparkstudio.zaixiyou.util;

import com.allsparkstudio.zaixiyou.ZaixiyouApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 陈帅
 * @date 2020/8/21
 */
@Slf4j
public class MailUtilsTest extends ZaixiyouApplicationTests {

    @Autowired
    MailUtils mailUtils;

    @Test
    public void sendSimpleMailTest() {
        mailUtils.sendSimpleMail("362774405@qq.com", "测试邮箱", "测试测试");
    }
}
