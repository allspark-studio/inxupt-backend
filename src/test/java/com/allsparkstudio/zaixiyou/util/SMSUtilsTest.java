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
public class SMSUtilsTest extends ZaixiyouApplicationTests {

    @Autowired
    SMSUtils smsUtils;

    @Test
    public void testSend() {
//        smsUtils.sendCode("18259234106");
    }
}
