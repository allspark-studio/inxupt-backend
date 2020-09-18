package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.consts.DefaultSettingConsts;
import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.enums.UserCircleRoleEnum;
import com.allsparkstudio.zaixiyou.pojo.form.AddCircleForm;
import com.allsparkstudio.zaixiyou.pojo.form.AddCircleNoticeForm;
import com.allsparkstudio.zaixiyou.pojo.po.Circle;
import com.allsparkstudio.zaixiyou.pojo.po.CircleNotice;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import com.allsparkstudio.zaixiyou.pojo.po.UserCircle;
import com.allsparkstudio.zaixiyou.pojo.vo.*;
import com.allsparkstudio.zaixiyou.service.CircleService;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 陈帅
 * @date 2020/8/24
 */
@Service
@Slf4j
public class CircleServiceImpl implements CircleService {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    UserMapper userMapper;

    @Autowired
    CircleMapper circleMapper;

    @Autowired
    UserCircleMapper userCircleMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    PostCircleMapper postCircleMapper;

    @Autowired
    CircleNoticeMapper circleNoticeMapper;

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public ResponseVO<Map<String, Integer>> addCircle(AddCircleForm addCircleForm, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        if (circleMapper.isNameExist(addCircleForm.getName())) {
            return ResponseVO.error(ResponseEnum.CIRCLE_NAME_EXISTS);
        }
        int userId = jwtUtils.getIdFromToken(token);
        Circle circle = new Circle();
        circle.setName(addCircleForm.getName());
        if (addCircleForm.getAvatarUrl() == null || StringUtils.isEmpty(addCircleForm.getAvatarUrl())) {
            circle.setAvatarUrl(DefaultSettingConsts.DEFAULT_CIRCLE_AVATAR);
        } else {
            circle.setAvatarUrl(addCircleForm.getAvatarUrl());
        }
        if (addCircleForm.getDescription() == null || StringUtils.isEmpty(addCircleForm.getDescription())) {
            circle.setDescription(DefaultSettingConsts.DEFAULT_CIRCLE_DESCRIPTION);
        } else {
            circle.setDescription(addCircleForm.getDescription());
        }
        if (addCircleForm.getBgImgUrl() == null || StringUtils.isEmpty(addCircleForm.getBgImgUrl())) {
            circle.setBgImgUrl(DefaultSettingConsts.DEFAULT_USERPAGE_BG_IMG_URL);
        } else {
            circle.setBgImgUrl(addCircleForm.getBgImgUrl());
        }
        int result = circleMapper.insertSelective(circle);
        if (result != 1) {
            log.error("新建圈子时出现错误,数据库表'circle'插入失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        // 通过MQ同步数据到ES
        rabbitTemplate.convertAndSend("MySQL2ESCircleExchange", "add", circle);
        UserCircle userCircle = new UserCircle();
        userCircle.setUserId(userId);
        userCircle.setCircleId(circle.getId());
        userCircle.setRole(UserCircleRoleEnum.OWNER.getCode());
        int result2 = userCircleMapper.insertSelective(userCircle);
        if (result2 != 1) {
            log.error("新建圈子时出现错误,数据库表'user_circle'插入失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        Map<String, Integer> map = new HashMap<>(1);
        map.put("circleId", circle.getId());
        return ResponseVO.success(map);
    }

    @Override
    public ResponseVO<List<CircleInListVO>> listCircles(Integer userId, String token) {
        List<Integer> circleIdList = userCircleMapper.selectCircleIdsByUserId(userId);
        List<CircleInListVO> circleVOList = new ArrayList<>();
        for (Integer circleId : circleIdList) {
            Circle circle = circleMapper.selectByPrimaryKey(circleId);
            if (circle == null) {
                log.error("列出用户圈子表时，'circle'表查询失败");
                return ResponseVO.error(ResponseEnum.ERROR);
            }
            CircleInListVO circleInListVO = new CircleInListVO();
            circleInListVO.setId(circle.getId());
            circleInListVO.setName(circle.getName());
            circleInListVO.setAvatarUrl(circle.getAvatarUrl());
            circleInListVO.setDescription(circle.getDescription());
            Integer members = userCircleMapper.countMembers(circleId);
            Integer topics = postCircleMapper.countPostsByCircleId(circleId);
            circleInListVO.setMembers(members);
            circleInListVO.setTopics(topics);
            circleInListVO.setSelected(false);
            circleVOList.add(circleInListVO);
        }
        return ResponseVO.success(circleVOList);
    }

    @Override
    public ResponseVO addNotice(Integer circleId, AddCircleNoticeForm form, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        int userId = jwtUtils.getIdFromToken(token);
        Circle circle = circleMapper.selectByPrimaryKey(circleId);
        if (circle == null) {
            log.error("请求的圈子不存在, circleId:[{}]", circleId);
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "圈子不存在");
        }
        Integer role = userCircleMapper.selectRoleOrNull(userId, circleId);
        if (role == null || role < UserCircleRoleEnum.ADMIN.getCode()) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION);
        }
        CircleNotice notice = new CircleNotice();
        notice.setCircleId(circleId);
        notice.setTitle(form.getTitle());
        notice.setBody(form.getBody());
        notice.setAuthorId(userId);
        notice.setState(form.getTop() ? 1 : 0);
        if (form.getMediaUrls() != null && form.getMediaUrls().size() != 0) {
            StringBuilder mediaUrlsBuilder = new StringBuilder();
            for (String mediaUrl : form.getMediaUrls()) {
                mediaUrlsBuilder.append(mediaUrl);
                mediaUrlsBuilder.append(";");
            }
            String mediaUrls = mediaUrlsBuilder.substring(0, mediaUrlsBuilder.length() - 1);
            notice.setMediaUrls(mediaUrls);
        }
        int result = circleNoticeMapper.insertSelective(notice);
        if (result != 1) {
            log.error("发布公告是出现错误,数据库表'circle_notice'插入失败");
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        return ResponseVO.success();
    }

    @Override
    public ResponseVO<CircleDetailVO> getCircle(Integer circleId, String token) {
        Integer userId = null;
        boolean login = false;
        if ((!StringUtils.isEmpty(token)) && (jwtUtils.validateToken(token))) {
            userId = jwtUtils.getIdFromToken(token);
            login = true;
        }
        Circle circle = circleMapper.selectByPrimaryKey(circleId);
        if (circle == null) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "圈子不存在");
        }
        CircleDetailVO circleDetailVO = new CircleDetailVO();
        circleDetailVO.setName(circle.getName());
        circleDetailVO.setAvatarUrl(circle.getAvatarUrl());
        circleDetailVO.setDescription(circle.getDescription());
        circleDetailVO.setBgImgUrl(circle.getBgImgUrl());
        Integer memberNum = userCircleMapper.countMembers(circleId);
        circleDetailVO.setMemberNum(memberNum);
        Integer topicNum = postCircleMapper.countPostsByCircleId(circleId);
        circleDetailVO.setTopicNum(topicNum);
        List<UserCircle> userCircleList = userCircleMapper.select5Members(circleId);
        List<CircleMemberVO> memberList = new ArrayList<>();
        for (UserCircle userCircle : userCircleList) {
            CircleMemberVO memberVO = new CircleMemberVO();
            memberVO.setId(userCircle.getUserId());
            memberVO.setRole(userCircle.getRole());
            User member = userMapper.selectByPrimaryKey(userCircle.getUserId());
            if (member == null) {
                log.error("请求圈子成员列表时出错，数据库表'user'查询失败，userId:[{}]", userCircle.getUserId());
                return ResponseVO.error(ResponseEnum.PARAM_ERROR, "圈子不存在");
            }
            memberVO.setNickName(member.getNickname());
            memberVO.setAvatarUrl(member.getAvatarUrl());
            memberList.add(memberVO);
        }
        circleDetailVO.setMemberList(memberList);
        CircleNotice firstNotice = circleNoticeMapper.selectFirstNotice(circleId);
        List<Map<String, Object>> notices = new ArrayList<>();
        Map<String, Object> firstNoticeMap = new HashMap<>(3);
        if (firstNotice != null) {
            firstNoticeMap.put("id", firstNotice.getId());
            firstNoticeMap.put("title", firstNotice.getTitle());
            if (firstNotice.getState() == 0) {
                firstNoticeMap.put("top", false);
            } else if (firstNotice.getState() == 1) {
                firstNoticeMap.put("top", true);
            }
            notices.add(firstNoticeMap);
        }

        CircleNotice secondNotice = circleNoticeMapper.selectSecondNotice(circleId);
        if (secondNotice != null) {
            Map<String, Object> secondNoticeMap = new HashMap<>(3);
            secondNoticeMap.put("id", secondNotice.getId());
            secondNoticeMap.put("title", secondNotice.getTitle());
            if (secondNotice.getState() == 0) {
                secondNoticeMap.put("top", false);
            } else if (secondNotice.getState() == 1) {
                secondNoticeMap.put("top", true);
            }
            notices.add(secondNoticeMap);
        }
        circleDetailVO.setNotices(notices);
        if (login) {
            Integer role = userCircleMapper.selectRoleOrNull(userId, circleId);
            if (role == null) {
                circleDetailVO.setRole(1);
            } else {
                circleDetailVO.setRole(role);
            }
        }
        return ResponseVO.success(circleDetailVO);
    }

    @Override
    public ResponseVO toggleFollow(Integer circleId, String token, boolean follow) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        Circle circle = circleMapper.selectByPrimaryKey(circleId);
        if (circle == null) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "圈子不存在");
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        // 1.关注
        if (follow) {
            Integer role = userCircleMapper.selectRoleOrNull(userId, circleId);
            // 管理员关注，提示已经是管理员
            if (UserCircleRoleEnum.ADMIN.getCode().equals(role)) {
                return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION, "您已经是管理员");
            } else if (UserCircleRoleEnum.OWNER.getCode().equals(role)) {
                return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION, "您已经是圈主");
            } else if (role == null || role < UserCircleRoleEnum.ADMIN.getCode()) {
                // 管理员以下的用户关注，直接改变role为follow
                int result = userCircleMapper.updateRole(userId, circleId, UserCircleRoleEnum.FOLLOW.getCode());
                if (result == 0 || result > 2) {
                    log.error("关注圈子时出现错误，数据库表'user_circle'更新失败");
                    return ResponseVO.error(ResponseEnum.ERROR);
                }
                return ResponseVO.success();
            }
        }
        // 2.取消关注
        else {
            Integer role = userCircleMapper.selectRoleOrNull(userId, circleId);
            // 圈主以下的用户取消关注，直接改变role为follow
            if (role == null || role < UserCircleRoleEnum.OWNER.getCode()) {
                int result = userCircleMapper.updateRole(userId, circleId, UserCircleRoleEnum.UNFOLLOW.getCode());
                if (result == 0 || result > 2) {
                    log.error("取消关注圈子时出现错误，数据库表'user_circle'更新失败");
                    return ResponseVO.error(ResponseEnum.ERROR);
                }
                return ResponseVO.success();
            }
            // 圈主取消关注，提醒不能取消
            if (UserCircleRoleEnum.OWNER.getCode().equals(role)) {
                return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION, "请先转让圈主或解散圈子");
            }
        }
        log.error("用户关注/取关圈子时发生未知错误, userId:[{}], circleId:[{}]", userId, circleId);
        return ResponseVO.error(ResponseEnum.ERROR);
    }

    @Override
    public ResponseVO<PageInfo> listAll(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Circle> circleList = circleMapper.selectAll();
        List<CircleInListVO> circleVOList = new ArrayList<>();
        for (Circle circle : circleList) {
            CircleInListVO circleInListVO = new CircleInListVO();
            circleInListVO.setId(circle.getId());
            circleInListVO.setName(circle.getName());
            circleInListVO.setAvatarUrl(circle.getAvatarUrl());
            circleInListVO.setDescription(circle.getDescription());
            Integer members = userCircleMapper.countMembers(circle.getId());
            Integer topics = postCircleMapper.countPostsByCircleId(circle.getId());
            circleInListVO.setMembers(members);
            circleInListVO.setTopics(topics);
            circleInListVO.setSelected(false);
            circleVOList.add(circleInListVO);
        }
        PageInfo pageInfo = new PageInfo<>(circleList);
        pageInfo.setList(circleVOList);
        return ResponseVO.success(pageInfo);
    }

    @Override
    public ResponseVO<List<NoticeInListVO>> listNotices(Integer circleId, String token) {
        List<CircleNotice> noticeList = circleNoticeMapper.selectAll(circleId);
        List<NoticeInListVO> noticeVOList = new ArrayList<>();
        for (CircleNotice notice : noticeList) {
            NoticeInListVO noticeInListVO = new NoticeInListVO();
            noticeInListVO.setId(notice.getId());
            noticeInListVO.setTitle(notice.getTitle());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            noticeInListVO.setPublishTime(simpleDateFormat.format(notice.getCreateTime()));
            noticeInListVO.setBody(notice.getBody());
            noticeInListVO.setAuthorId(notice.getAuthorId());
            User author = userMapper.selectByPrimaryKey(notice.getAuthorId());
            if (author == null) {
                noticeInListVO.setAuthorName("用户不存在");
            } else {
                noticeInListVO.setAuthorName(author.getNickname());
            }
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            noticeInListVO.setTop(notice.getState() == 1);
            noticeVOList.add(noticeInListVO);
        }
        return ResponseVO.success(noticeVOList);
    }

    @Override
    public ResponseVO<List<CircleMemberVO>> listMembers(Integer circleId, String token) {
        boolean login = false;
        Integer userId = null;
        if (jwtUtils.validateToken(token)) {
            login = true;
            userId = jwtUtils.getIdFromToken(token);
        }
        Circle circle = circleMapper.selectByPrimaryKey(circleId);
        if (circle == null) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "圈子不存在");
        }
        List<User> memberList = userMapper.selectAllMembersByCircleId(circleId);
        List<CircleMemberVO> circleMemberVOList = new ArrayList<>();
        for (User member : memberList) {
            CircleMemberVO memberVO = new CircleMemberVO();
            memberVO.setId(member.getId());
            Integer role = userCircleMapper.selectRoleOrNull(member.getId(), circleId);
            if (role == null) {
                role = 1;
            }
            memberVO.setRole(role);
            memberVO.setNickName(member.getNickname());
            memberVO.setAvatarUrl(member.getAvatarUrl());
            memberVO.setDescription(member.getDescription());
            boolean followed = false;
            if (login) {
                followed = followMapper.isFollowed(userId, member.getId());
            }
            memberVO.setFollowed(followed);
            circleMemberVOList.add(memberVO);
        }
        return ResponseVO.success(circleMemberVOList);
    }

    @Override
    public ResponseVO<NoticeVO> getNotice(Integer noticeId) {
        CircleNotice notice = circleNoticeMapper.selectByPrimaryKey(noticeId);
        if (notice == null) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "公告不存在");
        }
        NoticeVO noticeVO = new NoticeVO();
        noticeVO.setTitle(notice.getTitle());
        noticeVO.setBody(notice.getBody());
        if (notice.getMediaUrls() != null && !StringUtils.isEmpty(notice.getMediaUrls())) {
            List<String> mediaUrlList;
            String[] mediaUrl = notice.getMediaUrls().split(";");
            mediaUrlList = Arrays.asList(mediaUrl);
            noticeVO.setMediaUrls(mediaUrlList);
        }
        noticeVO.setTop(notice.getState() == 1);
        return ResponseVO.success(noticeVO);
    }

    @Override
    public ResponseVO deleteNotice(Integer noticeId, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        CircleNotice notice = circleNoticeMapper.selectByPrimaryKey(noticeId);
        if (notice == null) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "公告不存在");
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        Integer role = userCircleMapper.selectRoleOrNull(userId, notice.getCircleId());
        if (role == null || role < UserCircleRoleEnum.ADMIN.getCode()) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION, "没有权限");
        }
        int result = circleNoticeMapper.deleteByPrimaryKey(noticeId);
        if (result != 1) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        return ResponseVO.success();
    }

    @Override
    public ResponseVO toggleTopNotice(Integer noticeId, Boolean top, String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseVO.error(ResponseEnum.NEED_LOGIN);
        }
        if (!jwtUtils.validateToken(token)) {
            return ResponseVO.error(ResponseEnum.TOKEN_VALIDATE_FAILED);
        }
        CircleNotice notice = circleNoticeMapper.selectByPrimaryKey(noticeId);
        if (notice == null) {
            return ResponseVO.error(ResponseEnum.PARAM_ERROR, "公告不存在");
        }
        Integer userId = jwtUtils.getIdFromToken(token);
        Integer role = userCircleMapper.selectRoleOrNull(userId, notice.getCircleId());
        if (role == null || role < UserCircleRoleEnum.ADMIN.getCode()) {
            return ResponseVO.error(ResponseEnum.HAVE_NOT_PERMISSION, "没有权限");
        }
        notice.setState(top ? 1 : 0);
        int result = circleNoticeMapper.updateState(notice);
        if (result != 1) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }
        return ResponseVO.success();
    }
}
