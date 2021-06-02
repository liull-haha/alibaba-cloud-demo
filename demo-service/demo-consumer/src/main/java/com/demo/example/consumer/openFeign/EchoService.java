package com.demo.example.consumer.openFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author liull
 * @description
 * @date 2021/3/24
 */
@FeignClient(name = "demo-provider")
public interface EchoService {

    @RequestMapping(value = "/provider/echo/{string}", method = RequestMethod.GET)
    public String echo(@PathVariable String string);

}
