package com.allsparkstudio.zaixiyou.controller;

import com.allsparkstudio.zaixiyou.pojo.vo.MyPageInfo;
import com.allsparkstudio.zaixiyou.pojo.vo.PostVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.SearchService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLDecoder;


@RestController
@Slf4j
@Api(tags = "搜索")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search/posts")
    @ApiOperation("搜索帖子/文章")
    public ResponseVO<PageInfo> searchPosts(@RequestParam String keyWord,
                                            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                            @RequestHeader(value = "token", required = false) String token) throws IOException {
        keyWord = URLDecoder.decode(keyWord, "utf-8");
        return searchService.searchPosts(keyWord, pageNum, pageSize, token);
    }

    @GetMapping("/search/users")
    @ApiOperation("搜索用户")
    public ResponseVO<PageInfo> searchUsers(@RequestParam String keyWord,
                                            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                            @RequestHeader(value = "token", required = false) String token) throws IOException {
        keyWord = URLDecoder.decode(keyWord, "utf-8");
        return searchService.searchUsers(keyWord, pageNum, pageSize, token);
    }

    @GetMapping("/search/circles")
    @ApiOperation("搜索圈子")
    public ResponseVO<PageInfo> searchCircles(@RequestParam String keyWord,
                                            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                            @RequestHeader(value = "token", required = false) String token) throws IOException {
        keyWord = URLDecoder.decode(keyWord, "utf-8");
        return searchService.searchCircles(keyWord, pageNum, pageSize, token);
    }

    @GetMapping("/search/hotWords")
    @ApiOperation("热搜")
    public ResponseVO hotWords() {
        return searchService.getHotWords();
    }
}