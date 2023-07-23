package com.example.demopattern.singleton;

/**
 * @Author liull
 * @Date 2023/7/2 22:06
 * 静态内部类单例模式
 *
 **/
public class Singleton {

    private Singleton(){}

    private static class SingletonHolder{
        //内部静态类 只有被调用的时候才会被加载
        //静态属性被final修饰，保证只会被实例化一次，且严格保证实例化顺序
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance(){
        return SingletonHolder.INSTANCE;
    }
}
