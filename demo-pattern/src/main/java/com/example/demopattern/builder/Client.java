package com.example.demopattern.builder;

/**
 * @Author liull
 * @Date 2023/7/8 15:06
 **/
public class Client {
    public static void main(String[] args) {
        //建造者模式使用方式就是lombok的@Builder
        //详细使用见Bike.class
        Bike build = new Bike.BikeBuilder().frame("车架1").seat("车座2").build();
        System.out.println(build.toString());
    }
}
