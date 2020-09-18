package com.allsparkstudio.zaixiyou.service;

import com.allsparkstudio.zaixiyou.pojo.po.Circle;
import com.allsparkstudio.zaixiyou.pojo.po.Post;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import com.allsparkstudio.zaixiyou.pojo.vo.MyPageInfo;
import com.allsparkstudio.zaixiyou.pojo.vo.PostVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.github.pagehelper.PageInfo;

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
    ResponseVO<PageInfo> searchPosts(String keyWord, Integer pageNum, Integer pageSize, String token) throws IOException;

    /**
     * 搜索用户
     * @param keyWord 关键字
     * @param pageNum 分页页数
     * @param pageSize 每页条数
     * @param token token
     */
    ResponseVO<PageInfo> searchUsers(String keyWord, Integer pageNum, Integer pageSize, String token) throws IOException;

    /**
     * 搜索圈子
     * @param keyWord 关键字
     * @param pageNum 分页页数
     * @param pageSize 每页条数
     * @param token token
     */
    ResponseVO<PageInfo> searchCircles(String keyWord, Integer pageNum, Integer pageSize, String token) throws IOException;
}
