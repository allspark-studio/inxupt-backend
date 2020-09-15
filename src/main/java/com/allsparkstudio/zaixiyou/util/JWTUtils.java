package com.allsparkstudio.zaixiyou.util;

import com.allsparkstudio.zaixiyou.dao.UserMapper;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * jwt相关工具类
 *
 * @author 陈帅
 * @date 2020/8/13
 */
@Slf4j
@Component
public class JWTUtils {

    private static final String PAYLOAD_SUB = "sub";

    private static final String PAYLOAD_CREATED = "created";

    /**
     * 盐值
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * 过期时间，单位: 天
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    private final StringRedisTemplate redisTemplate = new StringRedisTemplate();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 根据负载生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000 * 3600 * 24);
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("JWT格式检验失败：[{}]", token);
        }
        return claims;
    }

    /**
     * 从token中获取用户id
     */
    public Integer getIdFromToken(String token) {
        Integer id;
        try {
            Claims claims = getClaimsFromToken(token);
            id = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            id = null;
            log.error("从token中解析用户id失败，Token:[{}]", token);
        }
        return id;
    }

    /**
     * 验证token是否有效
     *
     * @param token 客户端传入的token
     */
    public boolean validateToken(String token) {
        Integer id = getIdFromToken(token);
        if (id == null) {
            return false;
        }
        User user;
        try {
            user = userMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("校验token失败，用户不存在，userId:[{}]", id);
            return false;
        }
        if (user == null) {
            return false;
        }
        // MQ更新日活跃用户量
        // MQ更新用户最后登录时间
        rabbitTemplate.convertAndSend("dailyStatisticsExchange","userActive", user);
        log.debug("生产者routerKey=userActive发送消息, user:[{}]", user.toString());

        return id.equals(user.getId()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从Token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成Token
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(PAYLOAD_SUB, user.getId());
        claims.put(PAYLOAD_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 判断token是否可以被刷新
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(PAYLOAD_CREATED, new Date());
        return generateToken(claims);
    }
}
