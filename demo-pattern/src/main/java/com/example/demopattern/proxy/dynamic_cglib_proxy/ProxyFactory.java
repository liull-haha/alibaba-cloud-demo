package com.example.demopattern.proxy.dynamic_cglib_proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author liull
 * @Date 2023/7/8 23:23
 **/
public class ProxyFactory implements MethodInterceptor {

    private final TrianStation trianStation = new TrianStation();

    public TrianStation getProxyObject(){
        //Enhancer类 类似于JDK代理中的Proxy类
        Enhancer enhancer = new Enhancer();
        //代理对象是目标对象的子类，所以目标对象是父类
        enhancer.setSuperclass(TrianStation.class);
        //设置回调函数
        enhancer.setCallback(this);
        //返回创建的代理对象
        return (TrianStation) enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //代理对象增强目标对象的方法
        System.out.println("代理商收取相关服务费用");
        return method.invoke(trianStation,objects);
    }
}
