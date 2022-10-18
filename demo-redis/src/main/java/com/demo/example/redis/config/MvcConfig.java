package com.demo.example.redis.config;

import com.demo.example.redis.config.interceptor.LoginInterceptor;
import com.demo.example.redis.config.interceptor.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author liull
 * @date 2022/5/24 23:53
 * @desc
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).order(0);
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/user/code",
                        "/user/login",
                        "/show-type/**",
                        "/shop/**",
                        "/voucher/**",
                        "/upload/**",
                        "/blog/hit"
                )
                .order(1);
    }
}
