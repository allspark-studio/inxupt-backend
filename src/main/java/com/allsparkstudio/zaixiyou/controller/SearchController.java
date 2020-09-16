package com.allsparkstudio.zaixiyou.controller;

import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.SearchService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@Slf4j
@Api("搜索")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search/posts")
    public ResponseVO searchPosts(@RequestParam String keyWord,
                                  @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                  @RequestHeader(value = "token", required = false) String token) throws IOException {
        return searchService.searchPosts(keyWord, pageNum, pageSize, token);
    }
}
