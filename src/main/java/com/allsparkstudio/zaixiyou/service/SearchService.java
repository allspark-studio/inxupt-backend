package com.allsparkstudio.zaixiyou.service;

import com.allsparkstudio.zaixiyou.pojo.po.Circle;
import com.allsparkstudio.zaixiyou.pojo.po.Post;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;

import java.io.IOException;

/**
 * 搜索
 */
public interface SearchService {

    /**
     * 搜索帖子
     * @param keyWord 关键字
     * @param pageNum 分页页数
     * @param pageSize 每页条数
     * @param token token
     */
    ResponseVO searchPosts(String keyWord, Integer pageNum, Integer pageSize, String token) throws IOException;

    ResponseVO searchUsers(String keyWord, Integer pageNum, Integer pageSize, String token);

    ResponseVO searchCircles(String keyWord, Integer pageNum, Integer pageSize, String token);
}
