package com.allsparkstudio.zaixiyou.util.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信登录 Code2Session 接口响应体
 *
 * @author AlkaidChen
 * @date 2023/5/7
 */
@Data
public class Code2SessionResponse {

    // 用户唯一标识
    @SerializedName("openid")
    private String OpenID;

    // 会话密钥
    @SerializedName("session_key")
    private String SessionKey;

    // 用户在开放平台的唯一标识符
    @SerializedName("unionid")
    private String UnionID;

    // 错误码
    @SerializedName("errcode")
    private int ErrCode;

    // 错误信息
    @SerializedName("errmsg")
    private String ErrMsg;
}
