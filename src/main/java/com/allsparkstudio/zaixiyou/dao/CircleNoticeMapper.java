package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.Circle;
import com.allsparkstudio.zaixiyou.pojo.po.CircleNotice;

import java.util.List;

public interface CircleNoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CircleNotice record);

    int insertSelective(CircleNotice record);

    CircleNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CircleNotice record);

    int updateByPrimaryKey(CircleNotice record);

    List<CircleNotice> selectAll(Integer circleId);

    CircleNotice selectFirstNotice(Integer circleId);

    CircleNotice selectSecondNotice(Integer circleId);

    int updateState(CircleNotice record);
}