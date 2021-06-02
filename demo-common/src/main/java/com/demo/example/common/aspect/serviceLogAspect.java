package com.demo.example.common.aspect;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liull
 * @description
 * @date 2021/6/1
 */
@Component
@Aspect
@Slf4j
public class serviceLogAspect {

    @Pointcut(value = "execution(* com.demo.example.*.controller..*.*(..))")
    public void weblog() {
    }

    @Around(value = "weblog()")
    public Object aroundProduce(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 打印请求相关参数
        log.info("请求开始=================================");
        // 打印请求 url
        log.info("URL       : {}", request.getRequestURL().toString());
        // 打印 Http method
        log.info("请求方式   : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("调用方法   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求入参
        log.info("请求入参   : {}", JSONObject.toJSONString(joinPoint.getArgs()));
        //记录请求时间
        long startTime = System.currentTimeMillis();
        //调用方法
        Object result = joinPoint.proceed();
        // 打印出参
        log.info("请求出参  : {}", JSONObject.toJSONString(result));
        // 执行耗时
        log.info("执行消耗时间 : {} ms", System.currentTimeMillis() - startTime);
        log.info("请求结束====================================");
        return result;
    }

}
