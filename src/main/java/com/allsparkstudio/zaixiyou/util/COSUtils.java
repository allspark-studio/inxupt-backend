package com.allsparkstudio.zaixiyou.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.CreateBucketRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * 腾讯云COS工具类
 *
 * @author AlkaidChen
 * @date 2023/8/20
 */
@Component
@Slf4j
public class COSUtils {

    @Value("${qcloud.accessKeyId}")
    private String accessKeyId;

    @Value("${qcloud.accessKeySecret}")
    private String accessKeySecret;

    @Value("${qcloud.cos.region}")
    private String regionName;

    @Value("${qcloud.cos.bucketName}")
    private String bucketName;

    private static final String USER_UPLOAD_PATH_PREFIX = "user-media";

    public String upload(File file, Integer userId){
        log.info("COS文件上传开始：[{}]", file.getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String dateStr = format.format(new Date());

        COSCredentials cosCredentials = new BasicCOSCredentials(accessKeyId, accessKeySecret);
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cosCredentials, clientConfig);
        try {
            //如果容器不存在，就创建
            if(! cosClient.doesBucketExist(bucketName)){
                cosClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedAcl(CannedAccessControlList.PublicRead);
                cosClient.createBucket(createBucketRequest);
            }
            //创建文件路径
            String fileSuffix = file.getName().substring(file.getName().lastIndexOf('.'));
            String fileUrl = USER_UPLOAD_PATH_PREFIX + "/"
                    + userId + "/"
                    + dateStr + "/"
                    + UUID.randomUUID()
                    + fileSuffix;
            //上传文件
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileUrl, file);
            PutObjectResult result = cosClient.putObject(putObjectRequest);
            if(null != result){
                String cosURL = "https://" + bucketName + ".cos." + regionName + ".myqcloud.com/" + fileUrl;
                log.info("COS文件上传成功,COS地址：[{}]", cosURL);
                return cosURL;
            }
        } catch (CosServiceException cse){
            log.error("上传文件出错, CosServiceException: [{}]", cse.getMessage());
            cse.printStackTrace();
        } catch (CosClientException cce){
            log.error("上传文件出错, CosClientException: [{}]", cce.getMessage());
            cce.printStackTrace();
        } finally {
            //关闭
            cosClient.shutdown();
        }
        return null;
    }
}
