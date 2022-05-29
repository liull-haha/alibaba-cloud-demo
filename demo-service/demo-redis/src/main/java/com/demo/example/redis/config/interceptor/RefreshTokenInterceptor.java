package com.demo.example.redis.config.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.demo.example.redis.dto.UserDTO;
import com.demo.example.redis.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author liull
 * @date 2022/5/24 23:44
 * @desc
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中token
        String token = request.getHeader("authorization");
        //基于token获取redis中用户信息
        if(StrUtil.isBlank(token)){
            return true;
        }
        String tokenKey = "login:token:" + token;
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(tokenKey);
        if (entries .isEmpty()) {
            return true;
        }
        UserDTO userDTO = BeanUtil.toBean(entries, UserDTO.class);
        UserHolder.saveUser(userDTO);
        //刷新token在redis中有效期
        stringRedisTemplate.expire(tokenKey,30, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
