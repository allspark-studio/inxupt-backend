package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 陈帅
 * @date 2020/8/25
 */

@Data
public class CircleDetailVO {

    private String name;

    private Integer role;

    private String description;

    private String avatarUrl;

    private String bgImgUrl;

    private Integer memberNum;

    private Integer topicNum;

    private List<CircleMemberVO> memberList;

    private List<Map<String, Object>> announcements;

}
