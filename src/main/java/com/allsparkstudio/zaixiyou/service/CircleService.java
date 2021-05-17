package com.allsparkstudio.zaixiyou.service;

import com.allsparkstudio.zaixiyou.pojo.form.AddCircleForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddAnnouncementForm;
import com.allsparkstudio.zaixiyou.pojo.vo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author AlkaidChen
 * @date 2020/8/24
 */
public interface CircleService {

    /**
     * 新建圈子
     */
    ResponseVO<Map<String, Integer>> addCircle(AddCircleForm addCircleForm, String token);

    /**
     * 删除圈子
     */
    ResponseVO deleteCircle(Integer circleId, String token);


    /**
     * 根据id展示用户的圈子列表
     */
    ResponseVO<List<CircleInListVO>> listCircles(Integer userId, String token);

    /**
     * 发布公告
     */
    ResponseVO<Map<String, Integer>> addAnnouncement(Integer circleId, AddAnnouncementForm form, String token);

    /**
     * 圈子详情页
     */
    ResponseVO<CircleDetailVO> getCircle(Integer circleId, String token);

    /**
     * 切换圈子关注状态
     * @param circleId 圈子id
     * @param token token
     * @param follow true关注  false取关
     */
    ResponseVO toggleFollow(Integer circleId, String token, boolean follow);

    /**
     * 获取全部圈子
     */
    ResponseVO<PageInfo> listAll(Integer pageNum, Integer pageSize);

    /**
     * 获取圈子的全部公告列表
     */
    ResponseVO<List<AnnouncementInListVO>> listAnnouncements(Integer circleId, String token);

    /**
     * 列出圈子成员列表
     */
    ResponseVO<List<CircleMemberVO>> listMembers(Integer circleId, String token);

    /**
     * 公告详情
     */
    ResponseVO<AnnouncementVO> getAnnouncement(Integer announcementId);

    /**
     * 删除公告
     */
    ResponseVO deleteAnnouncement(Integer announcementId, String token);

    /**
     * 修改公告置顶状态
     */
    ResponseVO toggleTopAnnouncement(Integer announcementId, Boolean top, String token);
}
