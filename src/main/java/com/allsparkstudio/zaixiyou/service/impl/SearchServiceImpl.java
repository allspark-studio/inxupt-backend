package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.dao.*;
import com.allsparkstudio.zaixiyou.enums.PostTypeEnum;
import com.allsparkstudio.zaixiyou.pojo.po.*;
import com.allsparkstudio.zaixiyou.pojo.vo.CircleInListVO;
import com.allsparkstudio.zaixiyou.pojo.vo.UserVO;
import com.allsparkstudio.zaixiyou.pojo.vo.PostVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.SearchService;
import com.allsparkstudio.zaixiyou.util.JWTUtils;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    SearchWordMapper searchWordMapper;

    @Autowired
    PostMapper postMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserPostLikeMapper userPostLikeMapper;

    @Autowired
    UserPostFavoriteMapper userPostFavoriteMapper;

    @Autowired
    UserPostCoinMapper userPostCoinMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    CircleMapper circleMapper;

    @Autowired
    UserCircleMapper userCircleMapper;

    @Autowired
    PostCircleMapper postCircleMapper;

    @Override
    public ResponseVO<PageInfo> searchPosts(String keyWord, Integer pageNum, Integer pageSize, String token) throws IOException {
        boolean login = false;
        Integer myId = null;
        if (jwtUtils.validateToken(token)) {
            login = true;
            myId = jwtUtils.getIdFromToken(token);
        }
        SearchRequest request = new SearchRequest("post");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.multiMatchQuery(keyWord, "body", "title", "pureText"));
        // 分页
        sourceBuilder.from((pageNum - 1) * pageSize);
        sourceBuilder.size(pageSize);
        // 构建高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("body");
        highlightBuilder.field("title");
        highlightBuilder.field("pureText");
        highlightBuilder.preTags("<span style='color: #F76B8A'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        // 开始查询
        sourceBuilder.query(boolQueryBuilder);

        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField body = highlightFields.get("body");
            HighlightField title = highlightFields.get("title");
            HighlightField pureText = highlightFields.get("pureText");
            // 取出原来的结果
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            // 解析高亮的字段，并替换原来的字段
            if (body != null) {
                Text[] fragments = body.fragments();
                StringBuilder highlightedTextBuilder = new StringBuilder();
                for (Text text : fragments) {
                    highlightedTextBuilder.append(text);
                }
                sourceAsMap.put("body", highlightedTextBuilder.toString());
            }
            if (title != null) {
                Text[] fragments = title.fragments();
                StringBuilder highlightedTextBuilder = new StringBuilder();
                for (Text text : fragments) {
                    highlightedTextBuilder.append(text);
                }
                sourceAsMap.put("title", highlightedTextBuilder.toString());
            }
            if (pureText != null) {
                Text[] fragments = pureText.fragments();
                StringBuilder highlightedTextBuilder = new StringBuilder();
                for (Text text : fragments) {
                    highlightedTextBuilder.append(text);
                }
                sourceAsMap.put("pureText", highlightedTextBuilder.toString());
            }
            list.add(sourceAsMap);
        }
        // 构建分页的VO list对象
        List<PostVO> postVOList = new ArrayList<>();
        for (Map<String, Object> postMap : list) {
            Integer postId = (Integer) postMap.get("itemId");
            Post post = postMapper.selectByPrimaryKey(postId);
            // 如果不存在，说明ES与MySQL未同步
            if (post == null) {
                continue;
            }
            // 这部分代码和listAll的单个postVO的构建是一样的
            Integer postType = post.getType();
            PostVO postVO = new PostVO();
            postVO.setType(postType);
            if (PostTypeEnum.POST.getCode().equals(postType)) {
                if (post.getPostMediaUrls() != null && !StringUtils.isEmpty(post.getPostMediaUrls())) {
                    List<String> mediaUrlList;
                    String[] mediaUrl = post.getPostMediaUrls().split(";");
                    mediaUrlList = Arrays.asList(mediaUrl);
                    postVO.setMediaUrls(mediaUrlList);
                }
            }
            if (PostTypeEnum.ARTICLE.getCode().equals(postType)) {
                postVO.setTitle((String) postMap.get("title"));
                postVO.setPureText((String) postMap.get("pureText"));
                postVO.setCover(post.getArticleCover());
            }
            postVO.setPostId(post.getId());
            User author = userMapper.selectByPrimaryKey(post.getAuthorId());
            postVO.setAuthorId(author.getId());
            postVO.setAuthorName(author.getNickname());
            postVO.setAuthorAvatar(author.getAvatarUrl());
            postVO.setAuthorDescription(author.getDescription());
            postVO.setAuthorLevel(author.getLevel());
            postVO.setAccountAuth(Arrays.asList(author.getAccountAuth().split(";")));
            if (PostTypeEnum.POST.getCode().equals((Integer) postMap.get("type"))) {
                postVO.setBody((String) postMap.get("body"));
            } else {
                postVO.setBody(post.getBody());
            }
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            postVO.setCreateTime(simpleDateFormat.format(post.getCreateTime()));
            Integer likesNum = userPostLikeMapper.countByPostId(post.getId());
            Integer favoritesNum = userPostFavoriteMapper.countByPostId(post.getId());
            Integer coinsNum = userPostCoinMapper.countByPostId(post.getId());
            Integer commentsNum = commentMapper.countCommentsByPostId(post.getId());
            postVO.setLikeNum(likesNum);
            postVO.setFavoriteNum(favoritesNum);
            postVO.setCoinsNum(coinsNum);
            postVO.setCommentNum(commentsNum);
            boolean liked = false;
            boolean coined = false;
            boolean favorited = false;
            if (login) {
                UserPostLike userPostLike = userPostLikeMapper.selectByUserIdAndPostId(myId, post.getId());
                UserPostFavorite userPostFavorite = userPostFavoriteMapper.selectByUserIdAndPostId(myId, post.getId());
                UserPostCoin userPostCoin = userPostCoinMapper.selectByUserIdAndPostId(myId, post.getId());
                if (userPostLike != null && userPostLike.getState().equals(1)) {
                    liked = true;
                }
                if (userPostFavorite != null && userPostFavorite.getState().equals(1)) {
                    favorited = true;
                }
                if (userPostCoin != null && userPostCoin.getState().equals(1)) {
                    coined = true;
                }
            }
            postVO.setLiked(liked);
            postVO.setCoined(coined);
            postVO.setFavorited(favorited);

            // 设置@的用户
            if (post.getAtIds() != null && !StringUtils.isEmpty(post.getAtIds())) {
                List<Map<String, Object>> atMapList = new ArrayList<>();
                String[] atIds = post.getAtIds().split(";");
                for (String atId : atIds) {
                    User user = userMapper.selectByPrimaryKey(Integer.parseInt(atId));
                    Map<String, Object> atMap = new HashMap<>(2);
                    atMap.put("id", user.getId());
                    atMap.put("nickName", user.getNickname());
                    atMapList.add(atMap);
                }
                postVO.setAts(atMapList);
            }

            // 设置自定义标签
            if (post.getTags() != null && !StringUtils.isEmpty(post.getTags())) {
                List<String> tagList;
                String[] tags = post.getTags().split(";");
                tagList = Arrays.asList(tags);
                // 历史遗留原因导致要用map存
                List<Map<String, String>> tagMapList = new ArrayList<>();
                for (String tag : tagList) {
                    Map<String, String> tagMap = new HashMap<>(2);
                    tagMap.put("name", tag);
                    tagMapList.add(tagMap);
                }
                postVO.setTags(tagMapList);
            }

            postVOList.add(postVO);
        }
        // 构架分页对象
        PageInfo pageInfo = new PageInfo<>();
        pageInfo.setList(postVOList);
        long total = response.getHits().getTotalHits().value;
        pageInfo.setTotal(total);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPages((int) (total / pageSize + 1));
        pageInfo.setIsFirstPage(pageNum.equals(1));
        pageInfo.setHasNextPage(total > pageNum * pageSize);
        pageInfo.setIsLastPage(total <= pageNum * pageSize);
        return ResponseVO.success(pageInfo);
    }

    @Override
    public ResponseVO<PageInfo> searchUsers(String keyWord, Integer pageNum, Integer pageSize, String token) throws IOException {
        boolean login = false;
        Integer myId = null;
        if (jwtUtils.validateToken(token)) {
            login = true;
            myId = jwtUtils.getIdFromToken(token);
        }
        SearchRequest request = new SearchRequest("user");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", keyWord);
        // 分页
        sourceBuilder.from((pageNum - 1) * pageSize);
        sourceBuilder.size(pageSize);
        // 开始查询
        sourceBuilder.query(queryBuilder);

        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            // 取出结果
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            list.add(sourceAsMap);
        }
        // 构建分页的list对象
        List<UserVO> userVOList = new ArrayList<>();
        for (Map<String, Object> userMap : list) {
            Integer userId = (Integer) userMap.get("itemId");
            User user = userMapper.selectByPrimaryKey(userId);
            // 如果不存在，说明ES与MySQL未同步
            if (user == null) {
                continue;
            }
            // 这部分代码和关注/粉丝列表的VO构建一样
            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setAvatarUrl(user.getAvatarUrl());
            userVO.setDescription(user.getDescription());
            userVO.setNickName(user.getNickname());
            userVO.setSelected(false);
            if (login) {
                userVO.setFollowed(followMapper.isFollowed(myId, user.getId()));
            } else {
                userVO.setFollowed(false);
            }
            userVOList.add(userVO);
        }
        PageInfo pageInfo = new PageInfo<>();
        pageInfo.setList(userVOList);
        long total = response.getHits().getTotalHits().value;
        pageInfo.setTotal(total);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPages((int) (total / pageSize + 1));
        pageInfo.setIsFirstPage(pageNum.equals(1));
        pageInfo.setHasNextPage(total > pageNum * pageSize);
        pageInfo.setIsLastPage(total <= pageNum * pageSize);
        return ResponseVO.success(pageInfo);
    }

    @Override
    public ResponseVO<PageInfo> searchCircles(String keyWord, Integer pageNum, Integer pageSize, String token) throws IOException {
        SearchRequest request = new SearchRequest("circle");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", keyWord);
        // 分页
        sourceBuilder.from((pageNum - 1) * pageSize);
        sourceBuilder.size(pageSize);
        // 开始查询
        sourceBuilder.query(queryBuilder);

        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            // 取出结果
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            list.add(sourceAsMap);
        }
        // 构建分页的list对象
        List<CircleInListVO> circleVOList = new ArrayList<>();
        for (Map<String, Object> circleMap : list) {
            Integer circleId = (Integer) circleMap.get("itemId");
            Circle circle = circleMapper.selectByPrimaryKey(circleId);
            // 如果不存在，说明ES与MySQL未同步
            if (circle == null) {
                continue;
            }
            CircleInListVO circleVO = new CircleInListVO();
            circleVO.setId(circle.getId());
            circleVO.setName(circle.getName());
            circleVO.setAvatarUrl(circle.getAvatarUrl());
            circleVO.setDescription(circle.getDescription());
            Integer members = userCircleMapper.countMembers(circleId);
            Integer topics = postCircleMapper.countPostsByCircleId(circleId);
            circleVO.setMembers(members);
            circleVO.setTopics(topics);
            circleVO.setSelected(false);
            circleVOList.add(circleVO);
        }
        PageInfo pageInfo = new PageInfo<>();
        pageInfo.setList(circleVOList);
        long total = response.getHits().getTotalHits().value;
        pageInfo.setTotal(total);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPages((int) (total / pageSize + 1));
        pageInfo.setIsFirstPage(pageNum.equals(1));
        pageInfo.setHasNextPage(total > pageNum * pageSize);
        pageInfo.setIsLastPage(total <= pageNum * pageSize);
        return ResponseVO.success(pageInfo);
    }

    @Override
    public ResponseVO getHotWords() {
        List<String> topWords = searchWordMapper.selectTopWords();
        return ResponseVO.success(topWords);
    }
}
