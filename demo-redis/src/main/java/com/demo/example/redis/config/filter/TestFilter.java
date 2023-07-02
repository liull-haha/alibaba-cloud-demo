package com.demo.example.redis.config.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author liull
 * @description 测试过滤器
 * @date 2022/10/9
 */
@Slf4j
public class TestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init....");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("init....");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        log.info("destroy....");
    }
}
