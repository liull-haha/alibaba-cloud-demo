/*
package com.demo.example.provider.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

*/
/**
 * @author liull
 * @description
 * @date 2021/6/8
 *//*

@Configuration
public class DruidServletConfig {

    @Value("${spring.datasource.stat-view-servlet.login-username}")
    private String druidUserName;
    @Value("${spring.datasource.stat-view-servlet.login-password}")
    private String druidPassword;

    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String,String> initParams = new HashMap<>();
        initParams.put("loginUsername",druidUserName);//登录druid监控的账户
        initParams.put("loginPassword",druidPassword);//登录druid监控的密码
        initParams.put("allow","");//默认就是允许所有访问
        // initParams.put("deny","192.168.15.21");//黑名单的IP
        bean.setInitParameters(initParams);
        return bean;
    }
    //2、配置一个web监控的filter
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        Map<String,String> initParams = new HashMap<>();
        initParams.put("exclusions","*.js,*.css,/druid/*");
        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return  bean;
    }
}
*/
