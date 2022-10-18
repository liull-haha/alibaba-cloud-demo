package com.demo.example.redis.config;

import com.demo.example.redis.config.filter.TestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @author liull
 * @description
 * @date 2022/10/9
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TestFilter> testFilter(){
        FilterRegistrationBean<TestFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new TestFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("*"));
        return filterRegistrationBean;
    }
}
