package com.allsparkstudio.zaixiyou.util;

import com.allsparkstudio.zaixiyou.util.entity.Code2SessionResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @author AlkaidChen
 * @date 2023/5/7
 */
@Component
@Slf4j
public class WeChatUtils {

    @Value("${wechat.appID}")
    private String appID;

    @Value("${wechat.appSecret}")
    private String appSecret;

    private static final Gson gson = new Gson();

    // 微信开放平台登录 URL
    public static final String WECHAT_CODE_2_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 微信小程序登录
     * 通过微信临时登录凭证 code 获取用户 session 和 openID 以及用户在平台的 unionID
     * 详情参见微信小程序官方文档：https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
     *
     * @param code 前端获取的临时登录凭证
     */
    public Code2SessionResponse Code2Session(String code) throws Exception {
        //初始化三个基本的信息， client get response
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = new URIBuilder(WECHAT_CODE_2_SESSION_URL)
                .setParameter("appid", appID)
                .setParameter("secret", appSecret)
                .setParameter("js_code", code)
                // 登录时，grant_type 默认为 authorization_code，参见小程序文档
                .setParameter("grant_type", "authorization_code")
                .build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;

        //发送请求 获取响应
        response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() != 200) {
            // 提前抛出异常，保证此函数拿到的返回一定可用
            throw new HttpException("调用微信小程序api登录失败");
        }
        //得到httpResponse的实体数据
        HttpEntity httpEntity = response.getEntity();
        Code2SessionResponse result = gson.fromJson((EntityUtils.toString(httpEntity)), Code2SessionResponse.class);
        if (result.getErrCode() != 0) {
            throw new HttpException(result.getErrMsg());
        }
        return result;
    }
}
