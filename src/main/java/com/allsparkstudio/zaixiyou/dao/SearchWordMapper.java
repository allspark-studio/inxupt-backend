package com.allsparkstudio.zaixiyou.dao;

import com.allsparkstudio.zaixiyou.pojo.po.SearchWord;

import java.util.List;

public interface SearchWordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SearchWord record);

    int insertSelective(SearchWord record);

    SearchWord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SearchWord record);

    int updateByPrimaryKey(SearchWord record);

    List<String> selectTopWords();
}