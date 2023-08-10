package com.allsparkstudio.zaixiyou.pojo.po;

import java.io.Serializable;
import lombok.Data;

/**
 * collection
 * @author wzr
 * @date 2023/7/19
 */
@Data
public class Collection implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 话题id
     */
    private Integer topicId;

    /**
     * 合集名称
     */
    private String collectionName;

    /**
     * 合集图片url
     */
    private String collectionMediaUrls;

    private static final long serialVersionUID = 1L;
}