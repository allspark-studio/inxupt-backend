package com.allsparkstudio.zaixiyou.controller;

import com.allsparkstudio.zaixiyou.ZaixiyouApplication;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import com.allsparkstudio.zaixiyou.util.OSSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 其他的controller
 *
 * @author AlkaidChen
 * @date 2020/8/21
 */

@RestController
@Api(tags = "文件上传")
@Slf4j
public class FileController {


    @Autowired
    OSSUtils ossUtils;

    @Autowired
    JWTUtils jwtUtils;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public ResponseVO<String> upload(@ApiParam("file") @RequestParam(name = "file", required = false) MultipartFile file,
                                     @RequestHeader(value = "token", required = false) String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        log.info("文件上传");
        try {
            if (file.isEmpty()) {
                return ResponseVO.error(ResponseEnum.FILE_UPLOAD_FAILED, "文件上传失败，请选择文件");
            }
            if (file.getSize() > 1024 * 1024 * 20) {
                return ResponseVO.error(ResponseEnum.FILE_UPLOAD_FAILED, "文件大小不能大于20M");
            }
            String filename = file.getOriginalFilename();
            if (!"".equals(filename.trim())) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                String dateStr = format.format(new Date());
                File path = new File("./data/upload_temp/" + dateStr + "/");
                log.debug("文件路径：[{}]", path.toString());
                if (!path.exists()) {
                    path.mkdirs();
                }
                log.debug("新建文件夹的绝对路径：[{}]", path.getAbsolutePath());
                filename = path.getAbsolutePath() + "/" + filename;
                File newFile = new File(filename);
                log.debug("新文件路径:[{}]", newFile.getPath());
                FileOutputStream os = new FileOutputStream(newFile);
                os.write(file.getBytes());
                os.close();
                file.transferTo(newFile);
                //上传到OSS
                try {
                    String uploadUrl = ossUtils.upload(newFile, userId);
                    log.info("文件上传成功，url: [{}]", uploadUrl);
                    return ResponseVO.success(0, "上传成功", uploadUrl);
                } catch (Exception e) {
                    log.error("上传文件失败: [{}]", e.getMessage());
                    return ResponseVO.error(ResponseEnum.ERROR, "文件上传失败");
                }
            }
        } catch (Exception e) {
            log.error("文件上传失败: [{}]", e.getMessage());
            return ResponseVO.error(ResponseEnum.ERROR, "文件上传失败");
        }
        return ResponseVO.error(ResponseEnum.ERROR, "文件上传失败，发生未知错误");
    }
}