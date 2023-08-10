package com.allsparkstudio.zaixiyou.pojo.po;

import java.io.Serializable;
import lombok.Data;

/**
 * hotspot_post
 * @author @author wzr
 * @date 2023/7/19
 */
@Data
public class HotspotPost implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 帖子id
     */
    private Integer postId;

    /**
     * 合集id
     */
    private Integer collectionId;

    private static final long serialVersionUID = 1L;
}