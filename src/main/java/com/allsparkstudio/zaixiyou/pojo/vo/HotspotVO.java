package com.allsparkstudio.zaixiyou.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wzr
 * @date 2023/7/18
 */
@Data
public class HotspotVO {
    private String topicName;

    private List<CollectionVO> collectionNames;
}
