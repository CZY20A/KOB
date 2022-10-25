package com.kob.backend.config.filter;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 通过redis实现单点登录
 * 原理：
 * redis中存有最新登录的token，每次前端传来token验证两个token是否相同
 * 若不同，则再进行判断：前端传来的token中IssueAt(获取token时间)比较晚就放行
 * 否则代表了有人将该账号顶下
 */
@Component
public class RepeatLoginFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);

        if(jwt != null && !"null".equalsIgnoreCase(jwt) && jwt.length() > 0)
            jwt = jwt.substring(7); //前面带Bearer需要去掉

        //重复登录只校验带有合法jwt的  放过验证码以及免鉴权的那些请求
        if (null == jwt || "null".equalsIgnoreCase(jwt) || jwt.length() == 0) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isAccessAllowed(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 账号重复登录，跳转登录页面 logout
        logout(response);
    }

    private String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private boolean isAccessAllowed(String token) {
        String userId = "-1";
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
            String redisToken = redisTemplate.opsForValue().get(userId);

            //redis中没有token 第一次登录
            if(redisToken == null || "".equals(redisToken) || redisToken.length() == 0) {
                redisTemplate.opsForValue().set(userId, token);
                return true;
            }

            //两次token一致
            if(redisToken.equals(token)){
                return true;
            }

            Date date = claims.getIssuedAt();
            Date redisDate = JwtUtil.parseJWT(redisToken).getIssuedAt();
            if(date.after(redisDate)) { // 将新的token存入
                redisTemplate.opsForValue().set(userId, token);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void logout(HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try {
            JSONObject resultObj = new JSONObject();
            resultObj.put("data","账号已在别的地方登录，请重新登录");
            response.getWriter().print(resultObj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
