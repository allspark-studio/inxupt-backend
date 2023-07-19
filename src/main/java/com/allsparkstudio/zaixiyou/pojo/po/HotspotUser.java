package com.allsparkstudio.zaixiyou.pojo.po;

import java.io.Serializable;
import lombok.Data;

/**
 * hotspot_user
 * @author @author wzr
 * @date 2023/7/19
 */
@Data
public class HotspotUser implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    private static final long serialVersionUID = 1L;
}