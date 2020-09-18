package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.DailyStatistics;

public interface DailyStatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DailyStatistics record);

    int insertSelective(DailyStatistics record);

    DailyStatistics selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DailyStatistics record);

    int updateByPrimaryKey(DailyStatistics record);

    DailyStatistics selectByDate(String date);

    int updateActiveUserNum(DailyStatistics record);

    int updateRegisterUserNum(DailyStatistics record);
}