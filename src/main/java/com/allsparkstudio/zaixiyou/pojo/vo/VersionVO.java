package com.allsparkstudio.zaixiyou.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VersionVO {

    private Integer code;

    private String msg;

    private String version;

    private String url;

    private String log;

    private Integer force;
}
