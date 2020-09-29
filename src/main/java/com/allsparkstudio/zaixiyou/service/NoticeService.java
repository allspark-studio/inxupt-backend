package com.allsparkstudio.zaixiyou.service;


import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;

public interface NoticeService {
    ResponseVO countUnreadNotice(String token);

    ResponseVO listReplyNotices(String token);

    ResponseVO listNewsNotices(String token);

    ResponseVO listAtNotices(String token);

    ResponseVO listSystemNotices(String token);
}
