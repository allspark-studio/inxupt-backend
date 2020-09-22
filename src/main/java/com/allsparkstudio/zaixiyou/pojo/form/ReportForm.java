package com.allsparkstudio.zaixiyou.pojo.form;


import lombok.Data;

import java.util.List;

@Data
public class ReportForm {

    /**
     * 举报详情
     */
    private String detail;

    /**
     * 举报原因
     */
    private List<String> reasons;
}
