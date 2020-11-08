package com.allsparkstudio.zaixiyou.controller;

import com.allsparkstudio.zaixiyou.enums.ClubTypeEnum;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.ClubService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 社团实验室汇总Controller
 */
@RestController
@Api(tags = "社团实验室汇总")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @GetMapping("/clubs")
    @ApiOperation("获取社团列表")
    public ResponseVO<PageInfo> listClubs(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return clubService.listAll(ClubTypeEnum.CLUB.getCode(), pageNum, pageSize);
    }

    @GetMapping("/labs")
    @ApiOperation("获取实验室列表")
    public ResponseVO<PageInfo> listLabs(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return clubService.listAll(ClubTypeEnum.LAB.getCode(), pageNum, pageSize);
    }
}
