package com.allsparkstudio.zaixiyou.enums;

import lombok.Getter;

/**
 * @author 陈帅
 * @date 2020/7/23
 */
@Getter
public enum ResponseEnum {

    // 基本返回枚举
    SUCCESS(0, "请求成功"),
    ERROR(-1, "服务端错误"),
    PARAM_ERROR(1, "参数错误"),

    // 用户相关操作返回枚举
    USER_NAME_EXIST(101, "用户名已存在"),
    PHONE_EXIST(102, "手机号已注册"),
    USER_NOT_REGISTER(103, "用户未注册"),
    INCONSISTENT_PASSWORDS(104, "密码不一致"),
    CODE_NOT_MATCH(105, "验证码无效"),
    CODE_SENT_FREQUENTLY(106, "验证码发送过于频繁"),
    USERNAME_OR_PASSWORD_ERROR(107, "用户名或密码错误"),
    PASSWORD_IS_EMPTY(108, "密码为空"),
    // 跳转到登录页面
    TOKEN_VALIDATE_FAILED(109, "登录信息失效"),
    // 跳转到登录页面
    NEED_LOGIN(110, "请登录后再进行操作"),
    REGISTER_FAILED(111, "注册失败"),
    HAVE_NOT_PASSWORD(112,"请通过验证码登陆"),

    REACH_PUBLISH_LIMIT(113, "已达今日发布数量上限"),

    MUTE(114, "您已被禁言"),
    BANNED(115, "您的账号已被限制登录"),

    HAVE_NOT_PERMISSION(301, "没有权限"),
    CIRCLE_NAME_EXISTS(302, "圈子名字已存在"),


    COINS_NOT_ENOUGH(401, "硬币数量不足"),

    FILE_UPLOAD_FAILED(501, "文件上传失败");


    Integer code;

    String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
