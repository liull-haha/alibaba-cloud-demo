package com.demo.example.consumer.controller;

import com.demo.example.consumer.openFeign.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liull
 * @description
 * @date 2021/3/24
 */
@RestController("/test")
public class TestController {
    @Autowired
    private EchoService echoService;



    @RequestMapping(value = "/echo-feign/{str}", method = RequestMethod.GET)
    public String feign(@PathVariable String str) {
        return echoService.echo(str);
    }

}
