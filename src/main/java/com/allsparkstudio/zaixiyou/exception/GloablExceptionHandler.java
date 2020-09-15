package com.allsparkstudio.zaixiyou.exception;

import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.util.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 陈帅
 * @date 2020/8/14
 */
@RestControllerAdvice
@Slf4j
public class GloablExceptionHandler {

    @Autowired
    private MailUtils mailUtils;

    @ExceptionHandler(RuntimeException.class)
    public ResponseVO handle(RuntimeException e) {

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("发生未知错误，错误信息: ");
        contentBuilder.append(e.getMessage());
        contentBuilder.append("<br>错误栈信息：<br>");
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {

        contentBuilder.append(stackTraceElement.toString());
        contentBuilder.append("<br>");
        }
        String content = contentBuilder.toString();
        mailUtils.sendHtmlMail("362774405@qq.com", "你的辣鸡代码又双叒叕出BUG了！！！", content);

        log.error("发生未知错误，错误信息: [{}]", e.getMessage());
        log.error("start trace: ");
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            log.error(stackTraceElement.toString());
        }
        return ResponseVO.error(ResponseEnum.ERROR, "服务端发生未知错误");
    }
}
