package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.dao.ClubMapper;
import com.allsparkstudio.zaixiyou.pojo.po.Club;
import com.allsparkstudio.zaixiyou.pojo.vo.ClubInListVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.ClubService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {

    @Autowired
    private ClubMapper clubMapper;
    @Override
    public ResponseVO<PageInfo> listAll(Integer type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ClubInListVO> clubVOList = new ArrayList<>();
        List<Club> clubs = clubMapper.selectByType(type);
        for (Club club : clubs) {
            ClubInListVO clubVO = new ClubInListVO();
            clubVO.setAvatar(club.getAvatar());
            clubVO.setName(club.getName());
            clubVO.setCollege(club.getCollege());
            clubVO.setUserId(club.getUserId());
            clubVOList.add(clubVO);
        }
        PageInfo pageInfo = new PageInfo<>(clubVOList);
        pageInfo.setList(clubVOList);
        return ResponseVO.success(pageInfo);
    }
}
