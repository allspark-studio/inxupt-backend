package com.allsparkstudio.zaixiyou.controller;

import com.allsparkstudio.zaixiyou.pojo.form.AddArticleForm;
import com.allsparkstudio.zaixiyou.pojo.form.HotspotPostForm;
import com.allsparkstudio.zaixiyou.pojo.form.HotspotUserForm;
import com.allsparkstudio.zaixiyou.pojo.vo.HotspotPostVO;
import com.allsparkstudio.zaixiyou.pojo.vo.HotspotUserVO;
import com.allsparkstudio.zaixiyou.pojo.vo.HotspotVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.HotspotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 热点模块相关的controller
 *
 * @author wzr
 * @date 2023/7/18
 */
@Slf4j
@RestController
@Api(tags = "热点")
public class HotspotController {

    @Autowired
    private HotspotService hotspotService;

    @GetMapping("/hotspot/search")
    @ApiOperation("获取合集分类信息")
    public ResponseVO<List<HotspotVO>> getCollection(@RequestHeader(value = "token", required = false) String token) {
        return hotspotService.getCollection(token);
    }

    @GetMapping("/hotspot/postinfo")
    @ApiOperation("获取热点帖子信息")
    public ResponseVO<List<HotspotPostVO>> getPostinfo(@RequestBody HotspotPostForm hotspotPostForm,
                                                       @RequestHeader(value = "token", required = false) String token) {
        return hotspotService.getPostinfo(hotspotPostForm.getCollectionId(), hotspotPostForm.getPageNum(),
                hotspotPostForm.getPageSize(), hotspotPostForm.getSortedBy(), token);
    }

    @GetMapping("/hotspot/userinfo")
    @ApiOperation("获取热点用户信息")
    public ResponseVO<List<HotspotUserVO>> getUserinfo(@RequestBody HotspotUserForm hotspotUserForm,
                                                       @RequestHeader(value = "token", required = false) String token) {
        return hotspotService.getUserinfo(hotspotUserForm.getPageNum(), hotspotUserForm.getPageSize(),
                hotspotUserForm.getSortedBy(), token);
    }
}
