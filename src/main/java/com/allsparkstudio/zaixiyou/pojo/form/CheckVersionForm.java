package com.allsparkstudio.zaixiyou.pojo.form;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class CheckVersionForm {

    @Autowired
    private String version;

    @Autowired
    private String platForm;
}
