package com.allsparkstudio.zaixiyou.exception;

import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.log.ApiLog;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.util.MailUtils;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author AlkaidChen
 * @date 2020/8/14
 */
@RestControllerAdvice
@Slf4j
public class GloablExceptionHandler {

    @Autowired
    private MailUtils mailUtils;

    private static final Gson gson = new Gson();

    @ExceptionHandler(RuntimeException.class)
    public ResponseVO handle(RuntimeException e) {

        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录请求信息
        ApiLog apiLog = new ApiLog();
        apiLog.setIp(request.getRemoteAddr());
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = "none";
        }
        apiLog.setToken(token);
        apiLog.setMethod(request.getMethod());
        apiLog.setParameter(request.getParameterMap());
        apiLog.setStartTime(new Date());
        apiLog.setUrl(request.getRequestURL().toString());
        apiLog.setUrl(request.getRequestURL().toString());

        // 构建邮件信息
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("发生未知错误，请求信息: <br>");
        contentBuilder.append(gson.toJson(apiLog));
        contentBuilder.append("<br>错误信息：<br>");
        contentBuilder.append(e.getMessage());
        contentBuilder.append("<br>错误栈信息：<br>");
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {

            contentBuilder.append(stackTraceElement.toString());
            contentBuilder.append("<br>");
        }
        String content = contentBuilder.toString();
        mailUtils.sendHtmlMail("362774405@qq.com", "你的辣鸡代码又双叒叕出BUG了！！！", content);

        // 打印错误日志
        log.error("发生未知错误：请求信息:[{}]", gson.toJson(apiLog));
        log.error("错误信息: [{}]", e.getMessage());
        log.error("start trace: ");
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            log.error(stackTraceElement.toString());
        }
        return ResponseVO.error(ResponseEnum.ERROR, "服务端发生未知错误");
    }
}
