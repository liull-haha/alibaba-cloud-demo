package com.example.demopattern.factory;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @Author liull
 * @Date 2023/7/4 23:26
 **/
@Slf4j
public class CoffeeFactory {

    private static Map<String,Coffee> map = new HashMap<>();

    //读取配置文件，反射创建对象
    static {
        Properties properties = new Properties();
        InputStream resourceAsStream = CoffeeFactory.class.getClassLoader().getResourceAsStream("coffeebean.properties");
        try {
            properties.load(resourceAsStream);
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                Class clazz = Class.forName(properties.getProperty((String) key));
                Coffee coffee = (Coffee) clazz.newInstance();
                map.put((String) key,coffee);
            }
        } catch (Exception e) {
            log.error("初始化对象异常==",e);
        }
    }



    public static Coffee createCoffee(String name){
        return map.get(name);
    }

}
