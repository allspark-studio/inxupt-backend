package com.allsparkstudio.zaixiyou.controller;


import com.allsparkstudio.zaixiyou.dao.SMSMapper;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.po.SMS;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "发送短信")
public class SMSController {

    @Autowired
    SMSMapper smsMapper;

    @GetMapping("/sms/getTemplateDate")
    @ApiOperation("获取短信模板信息")
    public ResponseVO getTemplate() {
        List<SMS> smsList = smsMapper.selectAll();
        List<SMS> sendList = new ArrayList<>();
        for (SMS sms : smsList) {
            if (sms.getState() == 0) {
                sendList.add(sms);
            }
        }
        return ResponseVO.success(sendList);
    }

    @PostMapping("/sms/confirm/{id}")
    @ApiOperation("确认发送状态")
    public ResponseVO confirm(@PathVariable("id") Integer id,
                              @RequestParam Integer state) {
        SMS sms = smsMapper.selectByPrimaryKey(id);
        sms.setState(state);
        int resutl = smsMapper.updateByPrimaryKey(sms);
        if (resutl != 1) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        return ResponseVO.success();
    }
}
