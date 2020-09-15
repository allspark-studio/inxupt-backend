package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.dao.FeedbackMapper;
import com.allsparkstudio.zaixiyou.dao.VersionMapper;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.form.FeedbackForm;
import com.allsparkstudio.zaixiyou.pojo.po.Feedback;
import com.allsparkstudio.zaixiyou.pojo.po.Version;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.pojo.vo.VersionVO;
import com.allsparkstudio.zaixiyou.service.MetaService;
import com.allsparkstudio.zaixiyou.util.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
@Slf4j
public class MetaServiceImpl implements MetaService {


    @Autowired
    FeedbackMapper feedbackMapper;

    @Autowired
    MailUtils mailUtils;

    @Autowired
    private VersionMapper versionMapper;

    @Override
    public ResponseVO feedback(FeedbackForm feedbackForm) {
        Feedback feedback = new Feedback();
        feedback.setBody(feedbackForm.getBody());
        feedback.setContact(feedbackForm.getContact());
        int result = feedbackMapper.insertSelective(feedback);
        if (result != 1) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String content = "用户联系方式：" +
                feedbackForm.getContact() +
                "<br>" + "时间：" + simpleDateFormat.format(new Date()) +
                "<br>" + "正文：" + "<br>" +
                feedbackForm.getBody();
        mailUtils.sendHtmlMail("362774405@qq.com", "【在西邮】用户反馈", content);
        return ResponseVO.success("提交反馈成功");
    }

    @Override
    public ResponseVO<VersionVO> getVersion() {

        Version latestVersion = versionMapper.selectLatestVersion();
        if (latestVersion == null) {
            log.error("检测版本时出现错误，数据库最新版本为null");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        VersionVO versionVO = new VersionVO();
        versionVO.setVersion(latestVersion.getVersion());
        versionVO.setDescription(latestVersion.getVersion());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        versionVO.setPublishTime(simpleDateFormat.format(latestVersion.getPublishTime()));
        versionVO.setUrl(latestVersion.getUrl());
        return ResponseVO.success(versionVO);

    }
}
