package com.demo.example.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liull
 * @description
 * @date 2021/3/20
 */
@RestController
@RequestMapping("/provider")
public class EchoController {

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
    public String echo(@PathVariable String string){
        return string+"："+serverPort;
    }

}
