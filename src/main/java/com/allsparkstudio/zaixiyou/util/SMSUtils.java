package com.allsparkstudio.zaixiyou.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 阿里云的短信SMS
 * 详情见阿里云SMS服务文档:
 * https://api.aliyun.com/new#/?product=Dysmsapi
 * @author AlkaidChen
 * @date 2020/8/18
 */
@Slf4j
@Component
public class SMSUtils {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    private static final Gson gson = new Gson();

    public String sendCode(String phone) {

        String code = generateCode();


        // 阿里云sms短信验证码官方demo
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "在西邮");
        request.putQueryParameter("TemplateCode", "SMS_199805376");
        request.putQueryParameter("TemplateParam", paramToJson(code));
        try {
            CommonResponse response = client.getCommonResponse(request);
            JsonElement data = JsonParser.parseString(response.getData());
            if ("OK".equals(data.getAsJsonObject().get("Code").getAsString())) {
                stringRedisTemplate.opsForHash().put("code", phone, code);
                // 验证码5分钟过期
                stringRedisTemplate.expire("code", 5, TimeUnit.MINUTES);
                return code;
            }
            log.error("发送验证码错误：[{}]", response.getData());
            return null;
        } catch (ClientException e) {
            log.error("发送验证码错误：[{}]", e.getErrMsg());
            return null;
        }
    }

    /**
     * 生成一个六位数的验证码
     */
    private String generateCode() {
        int code = new Random().nextInt(999999);
        while (code < 100000) {
            code = new Random().nextInt(999999);
        }
        return String.valueOf(code);
    }

    public boolean validateCode(String phone, String code) {
        if (code.equals(stringRedisTemplate.opsForHash().get("code", phone))) {
            stringRedisTemplate.opsForHash().delete("code", phone);
            return true;
        }
        return false;
    }

    /**
     * 将参数转化为json格式
     */
    private String paramToJson(String code) {
        Map<String, String> map = new HashMap<>(1);
        map.put("code", code);
        return gson.toJson(map);
    }
}
