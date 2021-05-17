package com.allsparkstudio.zaixiyou.consts;

import java.util.Random;

/**
 * APP相关的默认配置
 * @author AlkaidChen
 */
public class DefaultSettingConsts {

    /**
     * 默认用户头像1（单头像）
     */
    public static final String DEFAULT_USER_AVATAR_URL_1 = "https://image.orenji.red/base-images/avatar.png";

    /**
     * 默认用户头像2（9选1）
     */
    public static String getDefaultAvatar() {
        return String.format("https://zaixiyou.oss-cn-beijing.aliyuncs.com/base-image/default-user-avatar/%d.jpg", new Random().nextInt(8));
    }

    /**
     * 默认用户签名
     */
    public static final String DEFAULT_USER_DESCRIPTION = "这个家伙很懒，什么都没有留下。";

    /**
     * 默认圈子头像
     */
    public static final String DEFAULT_CIRCLE_AVATAR = String.format("https://zaixiyou.oss-cn-beijing.aliyuncs.com/base-image/default-user-avatar/%d.jpg", new Random().nextInt(8));

    /**
     * 默认圈子简介
     */
    public static final String DEFAULT_CIRCLE_DESCRIPTION = "这个圈主瓜兮兮的，简介都不写，不要和他玩了";

    /**
     * 默认用户个人主页背景图片
     */
    public static final String DEFAULT_USERPAGE_BG_IMG_URL = "https://zaixiyou.oss-cn-beijing.aliyuncs.com/base-image/default-userpage-bg-img.png";
}
