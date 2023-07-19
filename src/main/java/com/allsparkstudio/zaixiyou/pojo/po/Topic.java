package com.allsparkstudio.zaixiyou.pojo.po;

import java.io.Serializable;
import lombok.Data;

/**
 * topic
 * @author wzr
 * @date 2023/7/19
 */
@Data
public class Topic implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 话题名称
     */
    private String topicName;

    private static final long serialVersionUID = 1L;
}