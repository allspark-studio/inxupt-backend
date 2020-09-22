package com.allsparkstudio.zaixiyou.service;

import com.allsparkstudio.zaixiyou.enums.UserContentStateEnum;
import com.allsparkstudio.zaixiyou.pojo.form.AddArticleForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddCircleArticleForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddCirclePostForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddPostForm;
import com.allsparkstudio.zaixiyou.pojo.vo.PostVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author 陈帅
 * @date 2020/8/20
 */
public interface PostService {

    /**
     * 按需展示全部帖子/文章列表
     * @param categoryId 按分类id展示
     * @param userId 按用户id展示
     * @param circleId 按圈子id展示
     * @param type 按类型展示(帖子/文章)
     * @param stateEnum 用户帖子行为，收藏，点赞等
     * @param token token
     * @param pageNum 第几页
     * @param pageSize 一页展示多少数据
     */
    ResponseVO<PageInfo> listAll(Integer categoryId, Integer userId, Integer circleId,
                                 Integer type, UserContentStateEnum stateEnum,
                                 String token, Integer pageNum, Integer pageSize,
                                 Integer sortedBy);

    /**
     * 给文章点赞
     * @param postId 文章id
     * @param token  token
     */
    ResponseVO like(Integer postId, String token);

    /**
     * 给文章取消点赞
     * @param postId 文章id
     * @param token  token
     */
    ResponseVO dislike(Integer postId, String token);

    /**
     * 收藏文章
     * @param postId 文章id
     * @param token  token
     */
    ResponseVO favorite(Integer postId, String token);

    /**
     * 取消收藏文章
     * @param postId 文章id
     * @param token  token
     */
    ResponseVO disFavorite(Integer postId, String token);

    /**
     * 投币文章
     * @param postId 文章id
     * @param token  token
     */
    ResponseVO coin(Integer postId, String token);

    /**
     * 发布帖子
     */
    ResponseVO<Map<String, Integer>> addPost(AddPostForm addPostForm, String token);

    /**
     * 发布文章
     */
    ResponseVO<Map<String, Integer>> addArticle(AddArticleForm addArticleForm, String token);

    /**
     * 圈子内发布帖子
     * @param circleId 圈子id
     */
    ResponseVO<Map<String, Integer>> addPostInCircle(Integer circleId, AddCirclePostForm form, String token);

    /**
     * 圈子内发布文章
     * @param circleId 圈子id
     */
    ResponseVO<Map<String, Integer>> addArticleInCircle(Integer circleId, AddCircleArticleForm form, String token);

    /**
     * 帖子、文章详情页
     * @param postId 帖子、文章id
     */
    ResponseVO<PostVO> getPost(Integer postId, String token);

    /**
     * 删除帖子或文章
     */
    ResponseVO deletePost(Integer postId, String token);
}
