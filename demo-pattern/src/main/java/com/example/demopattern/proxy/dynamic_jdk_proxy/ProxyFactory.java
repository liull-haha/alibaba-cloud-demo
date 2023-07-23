package com.example.demopattern.proxy.dynamic_jdk_proxy;

import com.example.demopattern.proxy.SellTickets;
import com.example.demopattern.proxy.TrianStation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @Author liull
 * @Date 2023/7/8 16:22
 * 获取代理对象的工厂类
 **/
public class ProxyFactory implements InvocationHandler{

    private TrianStation trianStation = new TrianStation();

    public SellTickets getProxyObject(){
        //jdk动态生成代理对象
        /**
         * 代理类也会实现 目标对象实现的接口
         * ClassLoader loader, 类加载器，用于加载代理类，可以通过目标对象获取类加载器
         * Class<?>[] interfaces, 代理类实现的接口的字节码对象数组
         * InvocationHandler h 代理对象调用的处理程序
         */
        return (SellTickets) Proxy.newProxyInstance(
                trianStation.getClass().getClassLoader(),
                trianStation.getClass().getInterfaces(),
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //代理对象的方法增强
        System.out.println("执行了"+method.getName()+"方法");
        System.out.println("方法参数："+ Arrays.toString(args));
        System.out.println("代理商收取服务费");
        return method.invoke(trianStation,args);
    }
}
