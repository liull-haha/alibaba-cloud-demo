package com.example.demopattern.factory;

/**
 * @Author liull
 * @Date 2023/7/4 23:42
 **/
public class Client {

    public static void main(String[] args) {
        Coffee latte = CoffeeFactory.createCoffee("latte");
        System.out.println(latte.getName());

        //jdk源码-Collection.iterator()使用的是工厂方法模式
        //Iterator是抽象产品，ArrayList.Itr是具体产品
        //Collection是抽象工厂，ArrayList是具体工厂

    }
}
