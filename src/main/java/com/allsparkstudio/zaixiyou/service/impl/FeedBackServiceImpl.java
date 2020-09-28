package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.dao.FeedbackMapper;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.form.FeedbackForm;
import com.allsparkstudio.zaixiyou.pojo.po.Feedback;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
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
public class FeedBackServiceImpl implements MetaService {


    @Autowired
    FeedbackMapper feedbackMapper;

    @Autowired
    MailUtils mailUtils;

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
}
