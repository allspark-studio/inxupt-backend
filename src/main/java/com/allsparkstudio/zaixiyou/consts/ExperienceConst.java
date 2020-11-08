package com.allsparkstudio.zaixiyou.consts;

/**
 * 用户等级与所需经验值
 * @author 陈帅
 * @date 2020/8/18
 */
public class ExperienceConst {
    public static Integer LV1 = 200;
    public static Integer LV2 = 1000;
    public static Integer LV3 = 5000;
    public static Integer LV4 = 20000;
    public static Integer LV5 = 60000;
    public static Integer LV6 = 9999999;

    /**
     * 根据等级获取对应升级需要的经验值
     */
    public static Integer getExpByLv(Integer level) {
        switch (level) {
            case 1:
                return LV1;
            case 2:
                return LV2;
            case 3:
                return LV3;
            case 4:
                return LV4;
            case 5:
                return LV5;
            case 6:
                return LV6;
            default:
                return 9999999;
        }
    }
}
