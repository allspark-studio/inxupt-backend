package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.SearchService;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.jsqlparser.expression.TimeValue;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class SearchServiceImpl implements SearchService {


    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public ResponseVO searchPosts(String keyWord, Integer pageNum, Integer pageSize, String token) throws IOException {
        boolean login = false;
        Integer userId;
        if (jwtUtils.validateToken(token)) {
            login = true;
            userId = jwtUtils.getIdFromToken(token);
        }
        SearchRequest request = new SearchRequest("post");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", keyWord);
        sourceBuilder.query(termQueryBuilder);

        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        gson.toJson(response.getHits());
        return ResponseVO.success(gson.toJson(response.getHits()));
    }

    @Override
    public ResponseVO searchUsers(String keyWord, Integer pageNum, Integer pageSize, String token) {
        return null;
    }

    @Override
    public ResponseVO searchCircles(String keyWord, Integer pageNum, Integer pageSize, String token) {
        return null;
    }
}
