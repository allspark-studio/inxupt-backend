package com.allsparkstudio.zaixiyou.service;

import com.allsparkstudio.zaixiyou.pojo.form.AddCommentForm;
import com.allsparkstudio.zaixiyou.pojo.vo.CommentVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;

import java.util.List;

/**
 * 评论相关
 * @author AlkaidChen
 * @date 2020/8/19
 */
public interface CommentService {
    /**
     * 列出一篇帖子的所有评论
     * @param postId 帖子的id
     * @param token token
     */
    ResponseVO<List<CommentVO>> listAll(Integer postId, String token, Integer sortedBy);

    /**
     * 发表评论
     * @param postId 帖子 id
     * @param token token
     * @param addCommentForm 提交表单内容
     */
    ResponseVO<CommentVO> addComment(Integer postId, String token, AddCommentForm addCommentForm);

    /**
     * 删除评论
     * @param commentId 评论id
     * @param token token
     */
    ResponseVO deleteComment(Integer commentId, String token);

    /**
     * 给评论点赞
     * @param commentId  评论id
     * @param token token
     */
    ResponseVO like(Integer commentId, String token);

    /**
     * 给评论取消点赞
     * @param commentId  评论id
     * @param token token
     */
    ResponseVO dislike(Integer commentId, String token);

    /**
     * 投币评论
     * @param commentId 评论id
     * @param token  token
     */
    ResponseVO coin(Integer commentId, String token);
}
