package com.allsparkstudio.zaixiyou.util;

import com.aliyun.oss.*;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * 阿里云OSS工具类
 * @author 陈帅
 * @date 2020/8/21
 */
@Component
@Slf4j
public class OSSUtils {

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String USER_UPLOAD_PATH_PREFIX = "user-media";

    public String upload(File file, Integer userId){
        log.info("OSS文件上传开始：[{}]", file.getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String dateStr = format.format(new Date());

        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        try {
            //如果容器不存在，就创建
            if(! ossClient.doesBucketExist(bucketName)){
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            //创建文件路径
            String fileSuffix = file.getName().substring(file.getName().lastIndexOf('.'));
            String fileUrl = USER_UPLOAD_PATH_PREFIX + "/"
                    + userId + "/"
                    + dateStr + "/"
                    + UUID.randomUUID().toString()
                    + fileSuffix;
            //上传文件
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileUrl, file);
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            if(null != result){
                log.info("OSS文件上传成功,OSS地址：[{}]", fileUrl);
                return "https://" + bucketName + "." + endpoint + "/" + fileUrl;
            }
        } catch (OSSException oe){
            log.error("上传文件出错, OSSException: [{}]", oe.getMessage());
        } catch (ClientException ce){
            log.error("上传文件出错, ClientException: [{}]", ce.getMessage());
        } finally {
            //关闭
            ossClient.shutdown();
        }
        return null;
    }

}
