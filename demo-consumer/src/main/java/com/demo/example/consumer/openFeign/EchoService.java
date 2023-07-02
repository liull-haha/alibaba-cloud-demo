package com.demo.example.consumer.openFeign;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author liull
 * @description
 * @date 2021/3/24
 */
@FeignClient(name = "demo-provider",fallbackFactory = EchoService.EchoServiceFallbackFactory.class)
public interface EchoService {

    @RequestMapping(value = "/provider/echo/{string}", method = RequestMethod.GET)
    public String echo(@PathVariable String string);


    @Slf4j
    @Component
    static class EchoServiceFallbackFactory implements FallbackFactory<EchoService>{
        @Override
        public EchoService create(Throwable throwable) {
            log.error("demo-provider error：",throwable);
            return new EchoService() {
                @Override
                public String echo(String string) {
                    return "fallback; reason was: " + throwable.getMessage();
                }
            };
        }
    }

}
