package com.allsparkstudio.zaixiyou.service;

import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.github.pagehelper.PageInfo;

public interface ClubService {
    ResponseVO<PageInfo> listAll(Integer type, Integer pageNum, Integer pageSize);
}
