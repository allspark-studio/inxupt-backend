package com.allsparkstudio.zaixiyou.util;

import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: Marble
 * @Date: 2021/6/2 17:09
 * @Description: 图片处理的相关工具
 */
public class ImageUtils {
    /**
     * 字节图片转base64
      * @param image
     * @return
     */
    public static String byteImageToBase64(byte[] image){
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(image);
    }
    /**
     * 网络图片转BASE64
     */
    public static String netImageToBase64(String netImagePath) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(netImagePath);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data.toByteArray());
    }

}
