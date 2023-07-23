package com.example.demopattern.singleton;

/**
 * @Author liull
 * @Date 2023/7/2 22:11
 **/
public class Client {

    public static void main(String[] args) {
        Singleton instance = Singleton.getInstance();
        Singleton instance1 = Singleton.getInstance();
        SingletonEnum instance2 = SingletonEnum.INSTANCE;
        SingletonEnum instance3 = SingletonEnum.INSTANCE;
//        System.out.println(instance == instance1);
//        System.out.println(instance2 == instance3);
    }

}
