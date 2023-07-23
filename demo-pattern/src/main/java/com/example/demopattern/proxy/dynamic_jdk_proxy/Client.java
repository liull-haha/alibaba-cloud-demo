package com.example.demopattern.proxy.dynamic_jdk_proxy;

import com.example.demopattern.proxy.SellTickets;

/**
 * @Author liull
 * @Date 2023/7/8 16:35
 **/
public class Client {

    public static void main(String[] args) {
        //生成 代理类的 工厂对象
        ProxyFactory proxyFactory = new ProxyFactory();
        //利用工厂类获取代理类
        SellTickets proxyObject = proxyFactory.getProxyObject();
        proxyObject.sellTickets(2);
        System.out.println("是否取到票："+proxyObject.pickUpTicket(1));

        /*System.out.println(proxyObject.getClass());
        while (true){

        }*/
    }
}

//启动程序后，内存中会动态生成代理类 下面为代理类的反编译class
//public final class $Proxy0
//        extends Proxy
//        implements SellTickets {
//    private static Method m4;
//    private static Method m3;
//
//    public final void sellTickets(Integer n) {
//        this.h.invoke(this, m3, new Object[]{n});
//    }
//
//    public final Boolean pickUpTicket(Integer n) {
//        return (Boolean)this.h.invoke(this, m4, new Object[]{n});
//    }
//
//    public $Proxy0(InvocationHandler invocationHandler) {
//        super(invocationHandler);
//    }
//
//    static {
//        m4 = Class.forName("com.example.demopattern.proxy.SellTickets").getMethod("pickUpTicket", Class.forName("java.lang.Integer"));
//        m3 = Class.forName("com.example.demopattern.proxy.SellTickets").getMethod("sellTickets", Class.forName("java.lang.Integer"));
//    }
//}


