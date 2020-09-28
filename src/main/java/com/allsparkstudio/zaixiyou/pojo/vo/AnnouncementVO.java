package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class AnnouncementVO {

    private String title;

    private String body;

    private List<String> mediaUrls;

    private Boolean top;
}
