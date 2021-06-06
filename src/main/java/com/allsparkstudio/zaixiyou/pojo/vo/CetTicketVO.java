package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

/**
 * @Author: Marble
 * @Date: 2021/6/2 16:37
 * @Description: 展示层显示cet准考证号等相关信息
 */
@Data
public class CetTicketVO {
    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 准考证号
     */
    private String testTicket;
}
