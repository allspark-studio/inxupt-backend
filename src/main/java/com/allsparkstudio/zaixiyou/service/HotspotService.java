package com.allsparkstudio.zaixiyou.service;

import com.allsparkstudio.zaixiyou.pojo.vo.HotspotPostVO;
import com.allsparkstudio.zaixiyou.pojo.vo.HotspotUserVO;
import com.allsparkstudio.zaixiyou.pojo.vo.HotspotVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;

import java.util.List;

/**
 * 热点相关
 *
 * @author wzr
 * @date 2023/7/18
 */
public interface HotspotService {

    /**
     * 返回合集分类
     */
    ResponseVO<List<HotspotVO>> getCollection(String token);

    /**
     * 获取热点帖子信息列表
     */
    ResponseVO<List<HotspotPostVO>> getPostinfo(Integer collectionId, Integer pageNum,
                                                Integer pageSize, Integer sortedBy, String token);

    /**
     * 获取热点用户信息列表
     */
    ResponseVO<List<HotspotUserVO>> getUserinfo(Integer pageNum, Integer pageSize,
                                                Integer sortedBy, String token);
}
