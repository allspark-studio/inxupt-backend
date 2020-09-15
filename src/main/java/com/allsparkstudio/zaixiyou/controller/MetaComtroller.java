package com.allsparkstudio.zaixiyou.controller;


import com.allsparkstudio.zaixiyou.pojo.form.FeedbackForm;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.MetaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 其他杂项等接口
 */
@RestController
@Api(tags = "其他")
@Slf4j
public class MetaComtroller {

    @Autowired
    MetaService metaService;

    @PostMapping("/feedback")
    @ApiOperation("反馈")
    public ResponseVO feedback(@RequestBody FeedbackForm form) {
        return metaService.feedback(form);
    }

    @GetMapping("/version")
    @ApiOperation("检测版本号")
    public ResponseVO getVersion() {
        return metaService.getVersion();
    }
}
