package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

/**
 * @Author: Marble
 * @Date: 2021/6/2 16:59
 * @Description: 展示层显示Cet成绩的相关信息
 */
@Data
public class CetGradeVO {
    /**
     * 总成绩
     */
    private String totalGrade;
    /**
     * 听力成绩
     */
    private String listeningGrade;
    /**
     * 阅读成绩
     */
    private String readingGrade;
    /**
     * 写作与翻译成绩
     */
    private String writingGrade;
    //应该还有口试成绩，暂时先留着
}
